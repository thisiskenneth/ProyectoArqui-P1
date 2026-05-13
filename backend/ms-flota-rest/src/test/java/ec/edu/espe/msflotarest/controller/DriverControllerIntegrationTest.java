package ec.edu.espe.msflotarest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.msflotarest.dto.DriverDto;
import ec.edu.espe.msflotarest.entity.Driver;
import ec.edu.espe.msflotarest.repository.DriverRepository;
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
class DriverControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
    }

    private DriverDto sampleDriver(String licenseNumber, boolean available) {
        DriverDto dto = new DriverDto();
        dto.setFirstName("Juan");
        dto.setLastName("Perez");
        dto.setLicenseNumber(licenseNumber);
        dto.setPhone("099-1234567");
        dto.setAvailable(available);
        return dto;
    }

    @Test
    void getAll_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_returnsCreatedAndPersists() throws Exception {
        DriverDto dto = sampleDriver("1710034065", true);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.licenseNumber").value("1710034065"));
    }

    @Test
    void create_withDuplicateLicense_returnsConflict() throws Exception {
        driverRepository.save(Driver.builder()
                .firstName("Ana")
                .lastName("Lopez")
                .licenseNumber("1710000009")
                .phone("099-0000000")
                .available(true)
                .build());

        DriverDto dto = sampleDriver("1710000009", true);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void create_withInvalidPayload_returnsBadRequest() throws Exception {
        DriverDto dto = new DriverDto();
        dto.setFirstName("");
        dto.setLastName("");
        dto.setLicenseNumber("");
        dto.setAvailable(null);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_existing_returnsDriver() throws Exception {
        Driver saved = driverRepository.save(Driver.builder()
                .firstName("Carlos")
                .lastName("Reyes")
                .licenseNumber("1710000017")
                .available(true)
                .build());

        mockMvc.perform(get("/api/drivers/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licenseNumber").value("1710000017"));
    }

    @Test
    void getById_missing_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/drivers/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getById_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/drivers/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void update_existing_updatesDriver() throws Exception {
        Driver saved = driverRepository.save(Driver.builder()
                .firstName("Old")
                .lastName("Name")
                .licenseNumber("1710000025")
                .available(true)
                .build());

        DriverDto updated = sampleDriver("1710000025", false);
        updated.setFirstName("Nuevo");

        mockMvc.perform(put("/api/drivers/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nuevo"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void update_missing_returnsNotFound() throws Exception {
        DriverDto dto = sampleDriver("1710000033", true);

        mockMvc.perform(put("/api/drivers/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existing_returnsNoContent() throws Exception {
        Driver saved = driverRepository.save(Driver.builder()
                .firstName("Del")
                .lastName("Ete")
                .licenseNumber("1710000041")
                .available(true)
                .build());

        mockMvc.perform(delete("/api/drivers/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missing_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/drivers/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailable_returnsOnlyAvailableDrivers() throws Exception {
        driverRepository.save(Driver.builder()
                .firstName("A").lastName("B").licenseNumber("1710000058").available(true).build());
        driverRepository.save(Driver.builder()
                .firstName("C").lastName("D").licenseNumber("1710000066").available(false).build());

        mockMvc.perform(get("/api/drivers/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].licenseNumber").value("1710000058"));
    }
}
