package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.client.MaintenanceTallerClient;
import ec.edu.espe.msflotarest.client.dto.TallerOrdenResponse;
import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.exception.DuplicateResourceException;
import ec.edu.espe.msflotarest.exception.ResourceNotFoundException;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final String NOT_FOUND_MESSAGE = "Vehículo no encontrado con id: ";
    private static final String AUTO_MAINTENANCE_DESCRIPTION = "Ingreso a mantenimiento programado";

    private final VehicleRepository vehicleRepository;
    private final MaintenanceTallerClient maintenanceTallerClient;

    public List<VehicleDto> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public VehicleDto findById(UUID id) {
        return vehicleRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE + id));
    }

    public VehicleDto save(VehicleDto dto) {
        if (vehicleRepository.existsByPlate(dto.getPlate())) {
            throw new DuplicateResourceException("Ya existe un vehículo con la matrícula: " + dto.getPlate());
        }
        Vehicle vehicle = convertToEntity(dto);
        return convertToDto(vehicleRepository.save(vehicle));
    }

    public VehicleDto update(UUID id, VehicleDto dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE + id));

        vehicleRepository.findByPlate(dto.getPlate()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Ya existe otro vehículo con la matrícula: " + dto.getPlate());
            }
        });

        VehicleStatus previousStatus = vehicle.getStatus();
        vehicle.setPlate(dto.getPlate());
        vehicle.setType(dto.getType());
        vehicle.setCapacityKg(dto.getCapacityKg());
        vehicle.setAutonomyKm(dto.getAutonomyKm());
        vehicle.setStatus(dto.getStatus());

        Vehicle saved = vehicleRepository.save(vehicle);
        VehicleDto result = convertToDto(saved);

        if (dto.getStatus() == VehicleStatus.MAINTENANCE && previousStatus != VehicleStatus.MAINTENANCE) {
            TallerOrdenResponse order = maintenanceTallerClient.registrar(
                    saved.getPlate(), AUTO_MAINTENANCE_DESCRIPTION);
            result.setMaintenanceOrderCode(order.getCodigoOrden());
        }

        return result;
    }

    public void delete(UUID id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE + id);
        }
        vehicleRepository.deleteById(id);
    }

    public List<VehicleDto> findAvailable() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(this::convertToDto)
                .toList();
    }

    private VehicleDto convertToDto(Vehicle entity) {
        VehicleDto dto = new VehicleDto();
        dto.setId(entity.getId());
        dto.setPlate(entity.getPlate());
        dto.setType(entity.getType());
        dto.setCapacityKg(entity.getCapacityKg());
        dto.setAutonomyKm(entity.getAutonomyKm());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private Vehicle convertToEntity(VehicleDto dto) {
        return Vehicle.builder()
                .plate(dto.getPlate())
                .type(dto.getType())
                .capacityKg(dto.getCapacityKg())
                .autonomyKm(dto.getAutonomyKm())
                .status(dto.getStatus())
                .build();
    }
}
