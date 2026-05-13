package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.entity.Driver;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.repository.DriverRepository;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    void getAvailability_returnsOnlyAvailableVehiclesAndDrivers() throws Exception {
        vehicleRepository.save(Vehicle.builder()
                .plate("AV-100").type("Auto").capacityKg(1000.0).status(VehicleStatus.AVAILABLE).build());
        vehicleRepository.save(Vehicle.builder()
                .plate("MN-100").type("Auto").capacityKg(1000.0).status(VehicleStatus.MAINTENANCE).build());

        driverRepository.save(Driver.builder()
                .firstName("On").lastName("Duty").licenseNumber("AV-DRV-1").available(true).build());
        driverRepository.save(Driver.builder()
                .firstName("Off").lastName("Duty").licenseNumber("AV-DRV-2").available(false).build());

        mockMvc.perform(get("/api/fleet/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicles", hasSize(1)))
                .andExpect(jsonPath("$.vehicles[0].plate").value("AV-100"))
                .andExpect(jsonPath("$.drivers", hasSize(1)))
                .andExpect(jsonPath("$.drivers[0].licenseNumber").value("AV-DRV-1"));
    }
}
