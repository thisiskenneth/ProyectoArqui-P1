package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAll() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<VehicleDto>> getAvailable() {
        return ResponseEntity.ok(vehicleService.findAvailable());
    }

    @PostMapping
    public ResponseEntity<VehicleDto> create(@Valid @RequestBody VehicleDto vehicleDto) {
        return new ResponseEntity<>(vehicleService.save(vehicleDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@PathVariable UUID id, @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.update(id, vehicleDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        vehicleService.delete(id);
    }
}
