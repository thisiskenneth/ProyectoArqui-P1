package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.dto.request.DriverRequest;
import ec.edu.espe.msflotarest.dto.response.DriverResponse;
import ec.edu.espe.msflotarest.entity.Driver;
import ec.edu.espe.msflotarest.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public List<DriverResponse> findAll() {
        return driverRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DriverResponse findById(Long id) {
        return driverRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    public DriverResponse save(DriverRequest request) {
        Driver driver = convertToEntity(request);
        return convertToResponse(driverRepository.save(driver));
    }

    public DriverResponse update(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
        
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setPhone(request.getPhone());
        driver.setAvailable(request.getAvailable());
        
        return convertToResponse(driverRepository.save(driver));
    }

    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new RuntimeException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }

    private DriverResponse convertToResponse(Driver entity) {
        DriverResponse response = new DriverResponse();
        response.setId(entity.getId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setLicenseNumber(entity.getLicenseNumber());
        response.setPhone(entity.getPhone());
        response.setAvailable(entity.getAvailable());
        return response;
    }

    private Driver convertToEntity(DriverRequest request) {
        return Driver.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .licenseNumber(request.getLicenseNumber())
                .phone(request.getPhone())
                .available(request.getAvailable())
                .build();
    }
}
