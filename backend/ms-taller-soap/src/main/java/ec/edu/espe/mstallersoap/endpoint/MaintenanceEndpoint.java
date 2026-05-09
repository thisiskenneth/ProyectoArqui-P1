package ec.edu.espe.mstallersoap.endpoint;

import ec.edu.espe.mstallersoap.model.ConsultarVehiculoRequest;
import ec.edu.espe.mstallersoap.model.ConsultarVehiculoResponse;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoRequest;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoResponse;
import ec.edu.espe.mstallersoap.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class MaintenanceEndpoint {
    private static final String NAMESPACE_URI = "http://espe.edu.ec/mstallersoap";
    private final MaintenanceService maintenanceService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarVehiculoRequest")
    @ResponsePayload
    public ConsultarVehiculoResponse consultarVehiculo(@RequestPayload ConsultarVehiculoRequest request) {
        return maintenanceService.consultar(request.getMatricula());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrarOrdenMantenimientoRequest")
    @ResponsePayload
    public RegistrarOrdenMantenimientoResponse registrarOrden(@RequestPayload RegistrarOrdenMantenimientoRequest request) {
        return maintenanceService.registrar(request.getMatricula(), request.getDescripcion());
    }
}
