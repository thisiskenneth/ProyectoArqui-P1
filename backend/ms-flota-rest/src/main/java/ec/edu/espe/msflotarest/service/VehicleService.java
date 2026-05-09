package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<VehicleDto> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VehicleDto findById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public VehicleDto save(VehicleDto dto) {
        Vehicle vehicle = convertToEntity(dto);
        return convertToDto(vehicleRepository.save(vehicle));
    }

    public VehicleDto update(Long id, VehicleDto dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        vehicle.setPlate(dto.getPlate());
        vehicle.setType(dto.getType());
        vehicle.setCapacityKg(dto.getCapacityKg());
        vehicle.setAutonomyKm(dto.getAutonomyKm());
        vehicle.setStatus(dto.getStatus());
        
        return convertToDto(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    public List<VehicleDto> findAvailable() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
