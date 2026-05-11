package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.dto.request.VehicleRequest;
import ec.edu.espe.msflotarest.dto.response.VehicleResponse;
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

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse findById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public VehicleResponse save(VehicleRequest request) {
        Vehicle vehicle = convertToEntity(request);
        return convertToResponse(vehicleRepository.save(vehicle));
    }

    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        vehicle.setPlate(request.getPlate());
        vehicle.setType(request.getType());
        vehicle.setCapacityKg(request.getCapacityKg());
        vehicle.setAutonomyKm(request.getAutonomyKm());
        vehicle.setStatus(request.getStatus());
        
        return convertToResponse(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
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
                .plate(request.getPlate())
                .type(request.getType())
                .capacityKg(request.getCapacityKg())
                .autonomyKm(request.getAutonomyKm())
                .status(request.getStatus())
                .build();
    }
}
