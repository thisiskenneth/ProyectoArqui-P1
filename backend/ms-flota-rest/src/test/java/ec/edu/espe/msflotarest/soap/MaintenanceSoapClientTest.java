package ec.edu.espe.msflotarest.soap;

import ec.edu.espe.msflotarest.exception.SoapServiceUnavailableException;
import ec.edu.espe.msflotarest.soap.model.ConsultarVehiculoResponse;
import ec.edu.espe.msflotarest.soap.model.RegistrarOrdenMantenimientoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaintenanceSoapClientTest {

    private final WebServiceTemplate template = mock(WebServiceTemplate.class);
    private final MaintenanceSoapClient client = new MaintenanceSoapClient(template);

    @Test
    void consultar_returnsResponseFromTemplate() {
        ConsultarVehiculoResponse stub = new ConsultarVehiculoResponse();
        stub.setMatricula("ABC-1");
        when(template.marshalSendAndReceive(any())).thenReturn(stub);

        ConsultarVehiculoResponse result = client.consultar("ABC-1");

        assertThat(result.getMatricula()).isEqualTo("ABC-1");
    }

    @Test
    void consultar_wrapsWebServiceIOExceptionAsServiceUnavailable() {
        when(template.marshalSendAndReceive(any()))
                .thenThrow(new WebServiceIOException("connect refused", new ConnectException()));

        assertThatThrownBy(() -> client.consultar("ABC-1"))
                .isInstanceOf(SoapServiceUnavailableException.class)
                .hasMessageContaining("taller");
    }

    @Test
    void registrar_returnsResponseFromTemplate() {
        RegistrarOrdenMantenimientoResponse stub = new RegistrarOrdenMantenimientoResponse();
        stub.setCodigoOrden("ORD-X");
        when(template.marshalSendAndReceive(any())).thenReturn(stub);

        RegistrarOrdenMantenimientoResponse result = client.registrar("ABC-1", "Revisión");

        assertThat(result.getCodigoOrden()).isEqualTo("ORD-X");
    }

    @Test
    void registrar_wrapsWebServiceIOExceptionAsServiceUnavailable() {
        when(template.marshalSendAndReceive(any()))
                .thenThrow(new WebServiceIOException("timeout", new ConnectException()));

        assertThatThrownBy(() -> client.registrar("ABC-1", "Revisión"))
                .isInstanceOf(SoapServiceUnavailableException.class);
    }
}
