package ec.edu.espe.msflotarest.service;

import ec.edu.espe.msflotarest.dto.DriverDto;
import ec.edu.espe.msflotarest.entity.Driver;
import ec.edu.espe.msflotarest.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public List<DriverDto> findAll() {
        return driverRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DriverDto findById(UUID id) {
        return driverRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + id));
    }

    public DriverDto save(DriverDto dto) {
        if (driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new IllegalArgumentException("Ya existe un conductor con la licencia: " + dto.getLicenseNumber());
        }
        Driver driver = convertToEntity(dto);
        return convertToDto(driverRepository.save(driver));
    }

    public DriverDto update(UUID id, DriverDto dto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + id));

        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setPhone(dto.getPhone());
        driver.setAvailable(dto.getAvailable());

        return convertToDto(driverRepository.save(driver));
    }

    public void delete(UUID id) {
        if (!driverRepository.existsById(id)) {
            throw new RuntimeException("Conductor no encontrado con id: " + id);
        }
        driverRepository.deleteById(id);
    }

    public List<DriverDto> findAvailable() {
        return driverRepository.findByAvailableTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DriverDto convertToDto(Driver entity) {
        DriverDto dto = new DriverDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setPhone(entity.getPhone());
        dto.setAvailable(entity.getAvailable());
        return dto;
    }

    private Driver convertToEntity(DriverDto dto) {
        return Driver.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .licenseNumber(dto.getLicenseNumber())
                .phone(dto.getPhone())
                .available(dto.getAvailable())
                .build();
    }
}
