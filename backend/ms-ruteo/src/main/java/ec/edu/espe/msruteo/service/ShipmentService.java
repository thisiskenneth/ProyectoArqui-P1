package ec.edu.espe.msruteo.service;

import ec.edu.espe.msruteo.client.FleetClient;
import ec.edu.espe.msruteo.client.TallerClient;
import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.VehicleResponse;
import ec.edu.espe.msruteo.dto.ShipmentEvent;
import ec.edu.espe.msruteo.dto.response.ShipmentResponse;
import ec.edu.espe.msruteo.entity.Shipment;
import ec.edu.espe.msruteo.entity.ShipmentStatus;
import ec.edu.espe.msruteo.publisher.EventPublisher;
import ec.edu.espe.msruteo.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final FleetClient fleetClient;
    private final TallerClient tallerClient;
    private final EventPublisher eventPublisher;

    @Transactional
    public ShipmentResponse assignShipment(AssignmentRequest request) {
        UUID orderId = UUID.fromString(request.getOrderId());
        log.info("Procesando asignación para pedido: {}", orderId);

        // Si ya existe un envío para este pedido, devolver el existente
        if (shipmentRepository.existsByOrderId(orderId)) {
            log.warn("El pedido {} ya tiene un envío asignado. Devolviendo existente.", orderId);
            return convertToResponse(shipmentRepository.findFirstByOrderId(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Envío no encontrado para pedido: " + orderId)));
        }

        // 1. Consultar vehículos disponibles en ms-flota-rest
        List<VehicleResponse> availableVehicles = fleetClient.getAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            throw new IllegalStateException("No hay vehículos disponibles para la asignación");
        }

        // 2. Lógica: tomar el primero disponible
        VehicleResponse vehicle = availableVehicles.get(0);

        // 3. Crear y guardar el envío
        Shipment shipment = Shipment.builder()
                .orderId(orderId)
                .vehicleId(vehicle.getId())
                .vehiclePlate(vehicle.getPlate())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(ShipmentStatus.ASSIGNED)
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Envío creado: {} para pedido: {}", savedShipment.getId(), orderId);

        // 4. Publicar evento
        ShipmentEvent event = ShipmentEvent.builder()
                .shipmentId(savedShipment.getId().toString())
                .orderId(savedShipment.getOrderId().toString())
                .driverName(null)
                .plate(savedShipment.getVehiclePlate())
                .customerEmail(request.getCustomerEmail())
                .build();
        eventPublisher.publishShipmentAssigned(event);

        // 5. Mantenimiento preventivo aleatorio 30%
        if (Math.random() < 0.3) {
            log.info("Mantenimiento preventivo sugerido para vehículo: {}", vehicle.getPlate());
            tallerClient.requestMaintenance(vehicle.getPlate(), "Mantenimiento preventivo tras asignación.");
        }

        return convertToResponse(savedShipment);
    }

    public List<ShipmentResponse> findAll() {
        return shipmentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ShipmentResponse findById(UUID id) {
        return shipmentRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Envío no encontrado con id: " + id));
    }

    public List<ShipmentResponse> findByOrderId(UUID orderId) {
        return shipmentRepository.findByOrderId(orderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ShipmentResponse convertToResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .orderId(shipment.getOrderId())
                .vehicleId(shipment.getVehicleId())
                .vehiclePlate(shipment.getVehiclePlate())
                .origin(shipment.getOrigin())
                .destination(shipment.getDestination())
                .status(shipment.getStatus())
                .assignedAt(shipment.getAssignedAt())
                .updatedAt(shipment.getUpdatedAt())
                .build();
    }
}
