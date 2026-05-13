package ec.edu.espe.mstallersoap.service;

import ec.edu.espe.mstallersoap.dto.VehiculoMaintenanceDto;
import ec.edu.espe.mstallersoap.dto.response.MantenimientoResponse;
import ec.edu.espe.mstallersoap.entity.MaintenanceOrder;
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
    public VehiculoMaintenanceDto consultar(String matricula) {
        return repository.findFirstByMatriculaOrderByFechaIngresoDesc(matricula)
                .map(order -> new VehiculoMaintenanceDto(
                        matricula,
                        "EN MANTENIMIENTO",
                        order.getFechaIngreso().toLocalDate().format(DATE_FORMATTER),
                        "Ultima orden: " + order.getCodigoOrden() + " - " + order.getDescripcion()))
                .orElseGet(() -> new VehiculoMaintenanceDto(
                        matricula,
                        "DISPONIBLE",
                        SIN_REGISTRO,
                        "Sin ordenes de mantenimiento registradas para esta matricula."));
    }

    @Transactional
    public MantenimientoResponse registrar(String matricula, String descripcion) {
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

        return new MantenimientoResponse(
                saved.getCodigoOrden(),
                saved.getFechaIngreso().toString(),
                "Orden registrada con exito para el vehiculo " + matricula + " (" + detalle + ")");
    }
}
