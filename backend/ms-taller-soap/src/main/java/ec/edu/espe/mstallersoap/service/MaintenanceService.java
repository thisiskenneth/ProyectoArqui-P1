package ec.edu.espe.mstallersoap.service;

import ec.edu.espe.mstallersoap.model.ConsultarVehiculoResponse;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MaintenanceService {

    public ConsultarVehiculoResponse consultar(String matricula) {
        ConsultarVehiculoResponse response = new ConsultarVehiculoResponse();
        response.setMatricula(matricula);
        response.setEstado("DISPONIBLE");
        response.setUltimoMantenimiento("2026-01-10");
        response.setObservaciones("Vehículo en buen estado. Próximo cambio de aceite en 500km.");
        return response;
    }

    public RegistrarOrdenMantenimientoResponse registrar(String matricula, String descripcion) {
        RegistrarOrdenMantenimientoResponse response = new RegistrarOrdenMantenimientoResponse();
        response.setCodigoOrden("ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        response.setFechaIngreso(LocalDateTime.now().toString());
        response.setMensaje("Orden registrada con éxito para el vehículo " + matricula);
        return response;
    }
}
