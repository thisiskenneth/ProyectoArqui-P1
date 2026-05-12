package ec.edu.espe.mstaller.service;

import ec.edu.espe.mstaller.dto.MaintenanceResponse;
import ec.edu.espe.mstaller.dto.VehicleStatusResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MaintenanceService {

    public VehicleStatusResponse consultar(String matricula) {
        return VehicleStatusResponse.builder()
                .matricula(matricula)
                .estado("DISPONIBLE")
                .ultimoMantenimiento("2026-01-10")
                .observaciones("Vehículo en buen estado. Próximo cambio de aceite en 500km.")
                .build();
    }

    public MaintenanceResponse registrar(String matricula, String descripcion) {
        return MaintenanceResponse.builder()
                .codigoOrden("ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .fechaIngreso(LocalDateTime.now().toString())
                .mensaje("Orden registrada con éxito para el vehículo " + matricula)
                .build();
    }
}
