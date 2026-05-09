package ec.edu.espe.msruteo.service;

import ec.edu.espe.msruteo.client.FleetClient;
import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.VehicleResponse;
import ec.edu.espe.msruteo.dto.ShipmentEvent;
import ec.edu.espe.msruteo.entity.Shipment;
import ec.edu.espe.msruteo.entity.ShipmentStatus;
import ec.edu.espe.msruteo.publisher.EventPublisher;
import ec.edu.espe.msruteo.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final FleetClient fleetClient;
    private final EventPublisher eventPublisher;

    public Shipment assignShipment(AssignmentRequest request) {
        log.info("Procesando asignación para pedido: {}", request.getOrderId());

        if (shipmentRepository.existsByOrderId(request.getOrderId())) {
            log.warn("El pedido {} ya tiene un envío asignado. Omitiendo.", request.getOrderId());
            return shipmentRepository.findByOrderId(request.getOrderId()).get(0);
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

        return savedShipment;
    }

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Shipment findById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));
    }

    public List<Shipment> findByOrderId(String orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }
}
