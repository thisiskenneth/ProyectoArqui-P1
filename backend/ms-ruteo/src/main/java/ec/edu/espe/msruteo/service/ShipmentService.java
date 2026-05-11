package ec.edu.espe.msruteo.service;

import ec.edu.espe.msruteo.client.FleetClient;
import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.VehicleResponse;
import ec.edu.espe.msruteo.dto.ShipmentEvent;
import ec.edu.espe.msruteo.dto.response.ShipmentResponse;
import ec.edu.espe.msruteo.entity.Shipment;
import ec.edu.espe.msruteo.entity.ShipmentStatus;
import ec.edu.espe.msruteo.publisher.EventPublisher;
import ec.edu.espe.msruteo.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final FleetClient fleetClient;
    private final EventPublisher eventPublisher;

    public ShipmentResponse assignShipment(AssignmentRequest request) {
        log.info("Procesando asignación para pedido: {}", request.getOrderId());

        if (shipmentRepository.existsByOrderId(request.getOrderId())) {
            log.warn("El pedido {} ya tiene un envío asignado. Omitiendo.", request.getOrderId());
            return convertToResponse(shipmentRepository.findByOrderId(request.getOrderId()).get(0));
        }

        // 1. Consultar vehículos disponibles en ms-flota-rest
        List<VehicleResponse> availableVehicles = fleetClient.getAvailableVehicles();

        if (availableVehicles.isEmpty()) {
            throw new RuntimeException("No hay vehículos disponibles para la asignación");
        }

        // 2. Lógica simple: tomar el primero
        VehicleResponse vehicle = availableVehicles.get(0);

        // 3. Crear y guardar el envío
        Shipment shipment = Shipment.builder()
                .orderId(request.getOrderId())
                .vehicleId(vehicle.getId())
                .vehiclePlate(vehicle.getPlate())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(ShipmentStatus.ASSIGNED)
                .assignedAt(LocalDateTime.now())
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);

        // 4. Publicar evento para ms-notificaciones y otros
        ShipmentEvent event = ShipmentEvent.builder()
                .shipmentId(savedShipment.getId().toString())
                .orderId(savedShipment.getOrderId())
                .driverName(null)
                .plate(savedShipment.getVehiclePlate())
                .customerEmail(request.getCustomerEmail())
                .build();

        eventPublisher.publishShipmentAssigned(event);

        return convertToResponse(savedShipment);
    }

    public List<ShipmentResponse> findAll() {
        return shipmentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ShipmentResponse findById(Long id) {
        return shipmentRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));
    }

    public List<ShipmentResponse> findByOrderId(String orderId) {
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
                .build();
    }
}
