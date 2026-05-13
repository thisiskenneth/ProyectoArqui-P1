package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.dto.request.VehicleRequest;
import ec.edu.espe.msflotarest.dto.response.VehicleResponse;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse findById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con id: " + id));
    }

    @Transactional
    public VehicleResponse save(VehicleRequest request) {
        // Regla de negocio: no registrar matrícula duplicada
        if (vehicleRepository.existsByPlateIgnoreCase(request.getPlate().toUpperCase())) {
            throw new IllegalStateException(
                "Ya existe un vehículo registrado con la matrícula: " + request.getPlate());
        }
        Vehicle vehicle = convertToEntity(request);
        vehicle.setPlate(request.getPlate().toUpperCase());
        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("Vehículo registrado: {} ({})", saved.getPlate(), saved.getType());
        return convertToResponse(saved);
    }

    @Transactional
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con id: " + id));

        // Regla: si se cambia la placa, verificar que no exista en otro vehículo
        if (!vehicle.getPlate().equalsIgnoreCase(request.getPlate())) {
            if (vehicleRepository.existsByPlateIgnoreCase(request.getPlate().toUpperCase())) {
                throw new IllegalStateException(
                    "Ya existe un vehículo con la matrícula: " + request.getPlate());
            }
        }

        // Regla: no cambiar estado de vehículo que está BUSY
        if (vehicle.getStatus() == VehicleStatus.BUSY && request.getStatus() != VehicleStatus.BUSY) {
            throw new IllegalStateException(
                "No se puede cambiar el estado de un vehículo que está actualmente en servicio (BUSY). " +
                "Primero debe completar o cancelar el envío activo.");
        }

        vehicle.setPlate(request.getPlate().toUpperCase());
        vehicle.setType(request.getType());
        vehicle.setCapacityKg(request.getCapacityKg());
        vehicle.setAutonomyKm(request.getAutonomyKm());
        vehicle.setStatus(request.getStatus());

        log.info("Vehículo actualizado: {}", vehicle.getPlate());
        return convertToResponse(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con id: " + id));

        // Regla: no eliminar vehículo en servicio
        if (vehicle.getStatus() == VehicleStatus.BUSY) {
            throw new IllegalStateException(
                "No se puede eliminar un vehículo que está actualmente en servicio (BUSY).");
        }

        vehicleRepository.deleteById(id);
        log.info("Vehículo eliminado: {}", vehicle.getPlate());
    }

    public List<VehicleResponse> findAvailable() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private VehicleResponse convertToResponse(Vehicle entity) {
        VehicleResponse response = new VehicleResponse();
        response.setId(entity.getId());
        response.setPlate(entity.getPlate());
        response.setType(entity.getType());
        response.setCapacityKg(entity.getCapacityKg());
        response.setAutonomyKm(entity.getAutonomyKm());
        response.setStatus(entity.getStatus());
        return response;
    }

    private Vehicle convertToEntity(VehicleRequest request) {
        return Vehicle.builder()
                .plate(request.getPlate().toUpperCase())
                .type(request.getType())
                .capacityKg(request.getCapacityKg())
                .autonomyKm(request.getAutonomyKm())
                .status(request.getStatus())
                .build();
    }
}
