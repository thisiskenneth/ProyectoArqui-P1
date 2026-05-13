package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.dto.DriverDto;
import ec.edu.espe.msflotarest.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverDto>> getAll() {
        return ResponseEntity.ok(driverService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(driverService.findById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverDto>> getAvailable() {
        return ResponseEntity.ok(driverService.findAvailable());
    }

    @PostMapping
    public ResponseEntity<DriverDto> create(@Valid @RequestBody DriverDto driverDto) {
        return new ResponseEntity<>(driverService.save(driverDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDto> update(@PathVariable UUID id, @Valid @RequestBody DriverDto driverDto) {
        return ResponseEntity.ok(driverService.update(id, driverDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        driverService.delete(id);
    }
}
