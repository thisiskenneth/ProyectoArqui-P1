package ec.edu.espe.mstallersoap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.mstallersoap.dto.request.MantenimientoRequest;
import ec.edu.espe.mstallersoap.entity.MaintenanceOrder;
import ec.edu.espe.mstallersoap.repository.MaintenanceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MaintenanceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MaintenanceOrderRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void getVehiculo_withNoHistory_returnsAvailable() throws Exception {
        mockMvc.perform(get("/api/vehiculos/{matricula}", "NUEVO-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value("NUEVO-1"))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
                .andExpect(jsonPath("$.ultimoMantenimiento").value("Sin registro"))
                .andExpect(jsonPath("$.observaciones").exists());
    }

    @Test
    void getVehiculo_withHistory_returnsLatestOrderInfo() throws Exception {
        repository.save(MaintenanceOrder.builder()
                .codigoOrden("ORD-AAA")
                .matricula("PLA-9")
                .descripcion("Cambio de aceite")
                .fechaIngreso(LocalDateTime.of(2026, 4, 20, 8, 0))
                .build());

        mockMvc.perform(get("/api/vehiculos/{matricula}", "PLA-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value("PLA-9"))
                .andExpect(jsonPath("$.estado").value("EN MANTENIMIENTO"))
                .andExpect(jsonPath("$.ultimoMantenimiento").value("2026-04-20"))
                .andExpect(jsonPath("$.observaciones").value(org.hamcrest.Matchers.containsString("ORD-AAA")));
    }

    @Test
    void registrarMantenimiento_validBody_returnsCreatedAndPersists() throws Exception {
        MantenimientoRequest body = new MantenimientoRequest();
        body.setMatricula("REG-1");
        body.setDescripcion("Revisión preventiva");

        mockMvc.perform(post("/api/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoOrden").exists())
                .andExpect(jsonPath("$.fechaIngreso").exists())
                .andExpect(jsonPath("$.mensaje").value(org.hamcrest.Matchers.containsString("REG-1")));

        org.assertj.core.api.Assertions.assertThat(
                repository.findFirstByMatriculaOrderByFechaIngresoDesc("REG-1")).isPresent();
    }

    @Test
    void registrarMantenimiento_blankFields_returnsBadRequest() throws Exception {
        MantenimientoRequest body = new MantenimientoRequest();
        body.setMatricula("");
        body.setDescripcion("");

        mockMvc.perform(post("/api/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerThenQuery_roundTripPersistsAndReadsBack() throws Exception {
        MantenimientoRequest body = new MantenimientoRequest();
        body.setMatricula("E2E-REST");
        body.setDescripcion("Diagnóstico");

        mockMvc.perform(post("/api/mantenimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/vehiculos/{matricula}", "E2E-REST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN MANTENIMIENTO"))
                .andExpect(jsonPath("$.observaciones").value(org.hamcrest.Matchers.containsString("Diagnóstico")));
    }
}
