package ec.edu.espe.mstallersoap.endpoint;

import ec.edu.espe.mstallersoap.entity.MaintenanceOrder;
import ec.edu.espe.mstallersoap.model.ConsultarVehiculoRequest;
import ec.edu.espe.mstallersoap.model.ConsultarVehiculoResponse;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoRequest;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoResponse;
import ec.edu.espe.mstallersoap.repository.MaintenanceOrderRepository;
import ec.edu.espe.mstallersoap.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MaintenanceEndpointTest {

    @Autowired
    private MaintenanceEndpoint endpoint;

    @Autowired
    private MaintenanceService service;

    @Autowired
    private MaintenanceOrderRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void consultar_withNoHistory_returnsAvailableWithSinRegistro() {
        ConsultarVehiculoRequest request = new ConsultarVehiculoRequest();
        request.setMatricula("NUEVO-1");

        ConsultarVehiculoResponse response = endpoint.consultarVehiculo(request);

        assertThat(response.getMatricula()).isEqualTo("NUEVO-1");
        assertThat(response.getEstado()).isEqualTo("DISPONIBLE");
        assertThat(response.getUltimoMantenimiento()).isEqualTo("Sin registro");
        assertThat(response.getObservaciones()).contains("Sin órdenes");
    }

    @Test
    void consultar_withPreviousOrder_returnsLatestOrderInfo() {
        repository.save(MaintenanceOrder.builder()
                .codigoOrden("ORD-OLD")
                .matricula("PLA-1")
                .descripcion("Cambio de filtro")
                .fechaIngreso(LocalDateTime.of(2026, 1, 10, 8, 0))
                .build());
        repository.save(MaintenanceOrder.builder()
                .codigoOrden("ORD-LAST")
                .matricula("PLA-1")
                .descripcion("Cambio de aceite")
                .fechaIngreso(LocalDateTime.of(2026, 4, 20, 8, 0))
                .build());

        ConsultarVehiculoRequest request = new ConsultarVehiculoRequest();
        request.setMatricula("PLA-1");

        ConsultarVehiculoResponse response = endpoint.consultarVehiculo(request);

        assertThat(response.getEstado()).isEqualTo("EN MANTENIMIENTO");
        assertThat(response.getUltimoMantenimiento()).isEqualTo("2026-04-20");
        assertThat(response.getObservaciones()).contains("ORD-LAST").contains("Cambio de aceite");
    }

    @Test
    void registrar_persistsOrderAndReturnsGeneratedCode() {
        RegistrarOrdenMantenimientoRequest request = new RegistrarOrdenMantenimientoRequest();
        request.setMatricula("XYZ-001");
        request.setDescripcion("Revisión completa");

        RegistrarOrdenMantenimientoResponse response = endpoint.registrarOrden(request);

        assertThat(response.getCodigoOrden()).startsWith("ORD-");
        assertThat(response.getFechaIngreso()).isNotBlank();
        assertThat(response.getMensaje()).contains("XYZ-001").contains("Revisión completa");

        assertThat(repository.findFirstByMatriculaOrderByFechaIngresoDesc("XYZ-001"))
                .isPresent()
                .get()
                .satisfies(saved -> {
                    assertThat(saved.getCodigoOrden()).isEqualTo(response.getCodigoOrden());
                    assertThat(saved.getDescripcion()).isEqualTo("Revisión completa");
                });
    }

    @Test
    void registrar_withBlankDescription_usesFallbackText() {
        RegistrarOrdenMantenimientoResponse blank = service.registrar("PLA-2", "");
        RegistrarOrdenMantenimientoResponse nullDesc = service.registrar("PLA-3", null);

        assertThat(blank.getMensaje()).contains("sin descripcion");
        assertThat(nullDesc.getMensaje()).contains("sin descripcion");
        assertThat(repository.findFirstByMatriculaOrderByFechaIngresoDesc("PLA-2"))
                .get().extracting(MaintenanceOrder::getDescripcion).isEqualTo("sin descripcion");
    }

    @Test
    void consultar_andRegistrar_endToEnd_persistsAndReadsBack() {
        RegistrarOrdenMantenimientoRequest registerRequest = new RegistrarOrdenMantenimientoRequest();
        registerRequest.setMatricula("E2E-1");
        registerRequest.setDescripcion("Diagnóstico general");
        RegistrarOrdenMantenimientoResponse registered = endpoint.registrarOrden(registerRequest);

        ConsultarVehiculoRequest consultRequest = new ConsultarVehiculoRequest();
        consultRequest.setMatricula("E2E-1");
        ConsultarVehiculoResponse consulted = endpoint.consultarVehiculo(consultRequest);

        assertThat(consulted.getEstado()).isEqualTo("EN MANTENIMIENTO");
        assertThat(consulted.getObservaciones()).contains(registered.getCodigoOrden());
    }
}
