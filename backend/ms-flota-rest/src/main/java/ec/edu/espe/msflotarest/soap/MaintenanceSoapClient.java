package ec.edu.espe.msflotarest.soap;

import ec.edu.espe.msflotarest.exception.SoapServiceUnavailableException;
import ec.edu.espe.msflotarest.soap.model.ConsultarVehiculoRequest;
import ec.edu.espe.msflotarest.soap.model.ConsultarVehiculoResponse;
import ec.edu.espe.msflotarest.soap.model.RegistrarOrdenMantenimientoRequest;
import ec.edu.espe.msflotarest.soap.model.RegistrarOrdenMantenimientoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
@RequiredArgsConstructor
public class MaintenanceSoapClient {

    private static final String UNAVAILABLE_MESSAGE = "El servicio de taller (SOAP) no está disponible.";

    private final WebServiceTemplate maintenanceWebServiceTemplate;

    public ConsultarVehiculoResponse consultar(String matricula) {
        ConsultarVehiculoRequest request = new ConsultarVehiculoRequest();
        request.setMatricula(matricula);
        return send(request, ConsultarVehiculoResponse.class);
    }

    public RegistrarOrdenMantenimientoResponse registrar(String matricula, String descripcion) {
        RegistrarOrdenMantenimientoRequest request = new RegistrarOrdenMantenimientoRequest();
        request.setMatricula(matricula);
        request.setDescripcion(descripcion);
        return send(request, RegistrarOrdenMantenimientoResponse.class);
    }

    private <T> T send(Object request, Class<T> responseType) {
        try {
            return responseType.cast(maintenanceWebServiceTemplate.marshalSendAndReceive(request));
        } catch (WebServiceIOException ex) {
            throw new SoapServiceUnavailableException(UNAVAILABLE_MESSAGE, ex);
        }
    }
}
