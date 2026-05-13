package ec.edu.espe.msflotarest.client;

import ec.edu.espe.msflotarest.exception.TallerServiceUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class MaintenanceTallerClientTest {

    private static final String BASE_URL = "http://taller-test/api";

    private MockRestServiceServer server;
    private MaintenanceTallerClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new MaintenanceTallerClient(builder.build());
    }

    @Test
    void consultar_returnsParsedResponse() {
        server.expect(requestTo(BASE_URL + "/vehiculos/ABC-1"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"matricula\":\"ABC-1\",\"estado\":\"DISPONIBLE\",\"ultimoMantenimiento\":\"2026-01-10\",\"observaciones\":\"OK\"}",
                        MediaType.APPLICATION_JSON));

        var result = client.consultar("ABC-1");

        assertThat(result.getMatricula()).isEqualTo("ABC-1");
        assertThat(result.getEstado()).isEqualTo("DISPONIBLE");
        assertThat(result.getUltimoMantenimiento()).isEqualTo("2026-01-10");
        server.verify();
    }

    @Test
    void registrar_sendsBodyAndReturnsParsedResponse() {
        server.expect(requestTo(BASE_URL + "/mantenimientos"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andRespond(withStatus(org.springframework.http.HttpStatus.CREATED)
                        .body("{\"codigoOrden\":\"ORD-X\",\"fechaIngreso\":\"2026-05-13T10:00:00\",\"mensaje\":\"ok\"}")
                        .contentType(MediaType.APPLICATION_JSON));

        var result = client.registrar("ABC-1", "Cambio de aceite");

        assertThat(result.getCodigoOrden()).isEqualTo("ORD-X");
        assertThat(result.getMensaje()).isEqualTo("ok");
        server.verify();
    }

    @Test
    void consultar_serverUnreachable_mapsToServiceUnavailable() {
        RestClient.Builder badBuilder = RestClient.builder().baseUrl("http://localhost:0/api");
        MaintenanceTallerClient unreachableClient = new MaintenanceTallerClient(badBuilder.build());

        assertThatThrownBy(() -> unreachableClient.consultar("ABC-1"))
                .isInstanceOf(TallerServiceUnavailableException.class)
                .hasMessageContaining("taller");
    }

    @Test
    void registrar_serverUnreachable_mapsToServiceUnavailable() {
        RestClient.Builder badBuilder = RestClient.builder().baseUrl("http://localhost:0/api");
        MaintenanceTallerClient unreachableClient = new MaintenanceTallerClient(badBuilder.build());

        assertThatThrownBy(() -> unreachableClient.registrar("ABC-1", "Revisión"))
                .isInstanceOf(TallerServiceUnavailableException.class);
    }
}
