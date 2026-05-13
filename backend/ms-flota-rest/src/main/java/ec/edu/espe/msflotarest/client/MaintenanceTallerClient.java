package ec.edu.espe.msflotarest.client;

import ec.edu.espe.msflotarest.client.dto.TallerOrdenRequest;
import ec.edu.espe.msflotarest.client.dto.TallerOrdenResponse;
import ec.edu.espe.msflotarest.client.dto.TallerVehiculoResponse;
import ec.edu.espe.msflotarest.exception.TallerServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MaintenanceTallerClient {

    private static final String UNAVAILABLE_MESSAGE = "El servicio de taller (REST) no está disponible.";

    private final RestClient tallerRestClient;

    public TallerVehiculoResponse consultar(String matricula) {
        try {
            return tallerRestClient.get()
                    .uri("/vehiculos/{matricula}", matricula)
                    .retrieve()
                    .body(TallerVehiculoResponse.class);
        } catch (ResourceAccessException ex) {
            throw new TallerServiceUnavailableException(UNAVAILABLE_MESSAGE, ex);
        }
    }

    public TallerOrdenResponse registrar(String matricula, String descripcion) {
        TallerOrdenRequest body = new TallerOrdenRequest(matricula, descripcion);
        try {
            return tallerRestClient.post()
                    .uri("/mantenimientos")
                    .body(body)
                    .retrieve()
                    .body(TallerOrdenResponse.class);
        } catch (ResourceAccessException ex) {
            throw new TallerServiceUnavailableException(UNAVAILABLE_MESSAGE, ex);
        }
    }
}
