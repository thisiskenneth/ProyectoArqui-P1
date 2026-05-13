package ec.edu.espe.mstallersoap.service;

import ec.edu.espe.mstallersoap.entity.MaintenanceOrder;
import ec.edu.espe.mstallersoap.model.ConsultarVehiculoResponse;
import ec.edu.espe.mstallersoap.model.RegistrarOrdenMantenimientoResponse;
import ec.edu.espe.mstallersoap.repository.MaintenanceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String SIN_REGISTRO = "Sin registro";

    private final MaintenanceOrderRepository repository;

    @Transactional(readOnly = true)
    public ConsultarVehiculoResponse consultar(String matricula) {
        ConsultarVehiculoResponse response = new ConsultarVehiculoResponse();
        response.setMatricula(matricula);

        repository.findFirstByMatriculaOrderByFechaIngresoDesc(matricula)
                .ifPresentOrElse(
                        order -> {
                            response.setEstado("EN MANTENIMIENTO");
                            response.setUltimoMantenimiento(order.getFechaIngreso().toLocalDate().format(DATE_FORMATTER));
                            response.setObservaciones("Última orden: " + order.getCodigoOrden() + " - " + order.getDescripcion());
                        },
                        () -> {
                            response.setEstado("DISPONIBLE");
                            response.setUltimoMantenimiento(SIN_REGISTRO);
                            response.setObservaciones("Sin órdenes de mantenimiento registradas para esta matrícula.");
                        });
        return response;
    }

    @Transactional
    public RegistrarOrdenMantenimientoResponse registrar(String matricula, String descripcion) {
        String detalle = (descripcion == null || descripcion.isBlank()) ? "sin descripcion" : descripcion;
        LocalDateTime now = LocalDateTime.now();
        String codigoOrden = "ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        MaintenanceOrder order = MaintenanceOrder.builder()
                .codigoOrden(codigoOrden)
                .matricula(matricula)
                .descripcion(detalle)
                .fechaIngreso(now)
                .build();
        MaintenanceOrder saved = repository.save(order);

        RegistrarOrdenMantenimientoResponse response = new RegistrarOrdenMantenimientoResponse();
        response.setCodigoOrden(saved.getCodigoOrden());
        response.setFechaIngreso(saved.getFechaIngreso().toString());
        response.setMensaje("Orden registrada con exito para el vehiculo " + matricula + " (" + detalle + ")");
        return response;
    }
}
