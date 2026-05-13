package ec.edu.espe.msflotarest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    private VehicleDto sampleVehicle(String plate, VehicleStatus status) {
        VehicleDto dto = new VehicleDto();
        dto.setPlate(plate);
        dto.setType("Camion");
        dto.setCapacityKg(1500.0);
        dto.setAutonomyKm(600.0);
        dto.setStatus(status);
        return dto;
    }

    @Test
    void getAll_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_returnsCreatedAndPersists() throws Exception {
        VehicleDto dto = sampleVehicle("ABC-123", VehicleStatus.AVAILABLE);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.plate").value("ABC-123"));
    }

    @Test
    void create_withDuplicatePlate_returnsConflict() throws Exception {
        vehicleRepository.save(Vehicle.builder()
                .plate("DUP-111")
                .type("Auto")
                .capacityKg(900.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        VehicleDto dto = sampleVehicle("DUP-111", VehicleStatus.AVAILABLE);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void create_withInvalidType_returnsBadRequest() throws Exception {
        VehicleDto dto = sampleVehicle("ABC-123", VehicleStatus.AVAILABLE);
        dto.setType("Avion");

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_existing_returnsVehicle() throws Exception {
        Vehicle saved = vehicleRepository.save(Vehicle.builder()
                .plate("XYZ-001")
                .type("Furgoneta")
                .capacityKg(2000.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        mockMvc.perform(get("/api/vehicles/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("XYZ-001"));
    }

    @Test
    void getById_missing_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/vehicles/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existing_updatesVehicle() throws Exception {
        Vehicle saved = vehicleRepository.save(Vehicle.builder()
                .plate("OLD-001")
                .type("Auto")
                .capacityKg(800.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        VehicleDto updated = sampleVehicle("OLD-001", VehicleStatus.MAINTENANCE);

        mockMvc.perform(put("/api/vehicles/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MAINTENANCE"));
    }

    @Test
    void update_withConflictingPlate_returnsConflict() throws Exception {
        vehicleRepository.save(Vehicle.builder()
                .plate("TAKEN-1")
                .type("Auto")
                .capacityKg(800.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        Vehicle other = vehicleRepository.save(Vehicle.builder()
                .plate("OTHER-1")
                .type("Auto")
                .capacityKg(800.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        VehicleDto updated = sampleVehicle("TAKEN-1", VehicleStatus.AVAILABLE);

        mockMvc.perform(put("/api/vehicles/{id}", other.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isConflict());
    }

    @Test
    void update_missing_returnsNotFound() throws Exception {
        VehicleDto dto = sampleVehicle("ZZ-999", VehicleStatus.AVAILABLE);

        mockMvc.perform(put("/api/vehicles/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existing_returnsNoContent() throws Exception {
        Vehicle saved = vehicleRepository.save(Vehicle.builder()
                .plate("DEL-001")
                .type("Auto")
                .capacityKg(800.0)
                .status(VehicleStatus.AVAILABLE)
                .build());

        mockMvc.perform(delete("/api/vehicles/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missing_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/vehicles/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailable_returnsOnlyAvailableVehicles() throws Exception {
        vehicleRepository.save(Vehicle.builder()
                .plate("AVA-1").type("Auto").capacityKg(800.0).status(VehicleStatus.AVAILABLE).build());
        vehicleRepository.save(Vehicle.builder()
                .plate("BSY-1").type("Auto").capacityKg(800.0).status(VehicleStatus.BUSY).build());

        mockMvc.perform(get("/api/vehicles/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plate").value("AVA-1"));
    }
}
