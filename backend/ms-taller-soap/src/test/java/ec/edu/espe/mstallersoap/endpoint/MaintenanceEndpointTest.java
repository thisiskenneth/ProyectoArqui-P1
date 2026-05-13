package ec.edu.espe.mstallersoap.endpoint;

import ec.edu.espe.mstallersoap.model.ConsultarVehiculoRequest;
import ec.edu.espe.mstallersoap.model.ConsultarVehiculoResponse;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoRequest;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoResponse;
import ec.edu.espe.mstallersoap.service.MaintenanceService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaintenanceEndpointTest {

    private final MaintenanceEndpoint endpoint = new MaintenanceEndpoint(new MaintenanceService());

    @Test
    void consultarVehiculo_returnsDefaultStatusAndEchoesPlate() {
        ConsultarVehiculoRequest request = new ConsultarVehiculoRequest();
        request.setMatricula("ABC-123");

        ConsultarVehiculoResponse response = endpoint.consultarVehiculo(request);

        assertThat(request.getMatricula()).isEqualTo("ABC-123");
        assertThat(response.getMatricula()).isEqualTo("ABC-123");
        assertThat(response.getEstado()).isEqualTo("DISPONIBLE");
        assertThat(response.getUltimoMantenimiento()).isNotBlank();
        assertThat(response.getObservaciones()).isNotBlank();
    }

    @Test
    void registrarOrden_withDescription_includesDescriptionInMessage() {
        RegistrarOrdenMantenimientoRequest request = new RegistrarOrdenMantenimientoRequest();
        request.setMatricula("XYZ-001");
        request.setDescripcion("Cambio de aceite");

        RegistrarOrdenMantenimientoResponse response = endpoint.registrarOrden(request);

        assertThat(request.getDescripcion()).isEqualTo("Cambio de aceite");
        assertThat(response.getCodigoOrden()).startsWith("ORD-");
        assertThat(response.getFechaIngreso()).isNotBlank();
        assertThat(response.getMensaje())
                .contains("XYZ-001")
                .contains("Cambio de aceite");
    }

    @Test
    void registrarOrden_withBlankDescription_usesFallbackText() {
        MaintenanceService service = new MaintenanceService();

        RegistrarOrdenMantenimientoResponse blank = service.registrar("PLA-1", "");
        RegistrarOrdenMantenimientoResponse nullDesc = service.registrar("PLA-2", null);

        assertThat(blank.getMensaje()).contains("PLA-1").contains("sin descripcion");
        assertThat(nullDesc.getMensaje()).contains("PLA-2").contains("sin descripcion");
    }
}
