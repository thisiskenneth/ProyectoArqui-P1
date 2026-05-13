package ec.edu.espe.msflotarest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.msflotarest.dto.request.MaintenanceOrderRequest;
import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import ec.edu.espe.msflotarest.exception.SoapServiceUnavailableException;
import ec.edu.espe.msflotarest.repository.VehicleRepository;
import ec.edu.espe.msflotarest.soap.MaintenanceSoapClient;
import ec.edu.espe.msflotarest.soap.model.ConsultarVehiculoResponse;
import ec.edu.espe.msflotarest.soap.model.RegistrarOrdenMantenimientoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleMaintenanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @MockitoBean
    private MaintenanceSoapClient maintenanceSoapClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Vehicle persistedVehicle;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        persistedVehicle = vehicleRepository.save(Vehicle.builder()
                .plate("MNT-PLATE")
                .type("Auto")
                .capacityKg(900.0)
                .status(VehicleStatus.AVAILABLE)
                .build());
    }

    @Test
    void getMaintenanceInfo_returnsDataFromSoap() throws Exception {
        ConsultarVehiculoResponse soap = new ConsultarVehiculoResponse();
        soap.setMatricula("MNT-PLATE");
        soap.setEstado("DISPONIBLE");
        soap.setUltimoMantenimiento("2026-01-10");
        soap.setObservaciones("Todo OK");
        when(maintenanceSoapClient.consultar("MNT-PLATE")).thenReturn(soap);

        mockMvc.perform(get("/api/vehicles/{id}/maintenance", persistedVehicle.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("MNT-PLATE"))
                .andExpect(jsonPath("$.status").value("DISPONIBLE"))
                .andExpect(jsonPath("$.lastMaintenance").value("2026-01-10"))
                .andExpect(jsonPath("$.notes").value("Todo OK"));

        verify(maintenanceSoapClient).consultar("MNT-PLATE");
    }

    @Test
    void getMaintenanceInfo_vehicleNotFound_returns404() throws Exception {
        mockMvc.perform(get("/api/vehicles/{id}/maintenance", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMaintenanceInfo_soapDown_returns503() throws Exception {
        when(maintenanceSoapClient.consultar(any()))
                .thenThrow(new SoapServiceUnavailableException("SOAP no disponible", null));

        mockMvc.perform(get("/api/vehicles/{id}/maintenance", persistedVehicle.getId()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createMaintenanceOrder_returnsCreatedWithSoapPayload() throws Exception {
        RegistrarOrdenMantenimientoResponse soap = new RegistrarOrdenMantenimientoResponse();
        soap.setCodigoOrden("ORD-ABC123");
        soap.setFechaIngreso("2026-05-13T11:00:00");
        soap.setMensaje("Orden creada");
        when(maintenanceSoapClient.registrar(eq("MNT-PLATE"), eq("Cambio de aceite"))).thenReturn(soap);

        MaintenanceOrderRequest body = new MaintenanceOrderRequest();
        body.setDescripcion("Cambio de aceite");

        mockMvc.perform(post("/api/vehicles/{id}/maintenance-orders", persistedVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoOrden").value("ORD-ABC123"))
                .andExpect(jsonPath("$.fechaIngreso").value("2026-05-13T11:00:00"))
                .andExpect(jsonPath("$.mensaje").value("Orden creada"));

        verify(maintenanceSoapClient).registrar("MNT-PLATE", "Cambio de aceite");
    }

    @Test
    void createMaintenanceOrder_blankDescription_returnsBadRequest() throws Exception {
        MaintenanceOrderRequest body = new MaintenanceOrderRequest();
        body.setDescripcion("");

        mockMvc.perform(post("/api/vehicles/{id}/maintenance-orders", persistedVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMaintenanceOrder_vehicleNotFound_returns404() throws Exception {
        MaintenanceOrderRequest body = new MaintenanceOrderRequest();
        body.setDescripcion("Revisión general");

        mockMvc.perform(post("/api/vehicles/{id}/maintenance-orders", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMaintenanceOrder_soapDown_returns503() throws Exception {
        when(maintenanceSoapClient.registrar(any(), any()))
                .thenThrow(new SoapServiceUnavailableException("SOAP caído", null));

        MaintenanceOrderRequest body = new MaintenanceOrderRequest();
        body.setDescripcion("Revisión");

        mockMvc.perform(post("/api/vehicles/{id}/maintenance-orders", persistedVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503));
    }
}
