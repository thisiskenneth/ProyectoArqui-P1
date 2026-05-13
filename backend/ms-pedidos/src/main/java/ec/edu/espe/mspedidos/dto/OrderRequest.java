package ec.edu.espe.mspedidos.dto;

import ec.edu.espe.mspedidos.entity.GeographicLevel;
import ec.edu.espe.mspedidos.entity.VehicleType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    @NotBlank(message = "El ID del cliente es obligatorio")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
             message = "El ID del cliente debe ser un UUID válido")
    private String clienteId;

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El email del cliente debe tener formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String customerEmail;

    @NotNull(message = "La lista de ítems es obligatoria")
    @Size(min = 1, message = "El pedido debe contener al menos un ítem")
    @Size(max = 50, message = "El pedido no puede contener más de 50 ítems")
    private List<@NotBlank(message = "El ítem no puede estar vacío")
                 @Size(max = 255, message = "La descripción del ítem no puede superar 255 caracteres") String> items;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a 0")
    @DecimalMax(value = "999999.99", message = "El total no puede superar 999,999.99")
    @Digits(integer = 8, fraction = 2, message = "El total debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal total;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0 kg")
    @DecimalMax(value = "20000.0", message = "El peso no puede superar 20,000 kg")
    private Double weightKg;

    @NotNull(message = "El nivel geográfico es obligatorio (LOCAL, PROVINCIAL, NATIONAL)")
    private GeographicLevel geographicLevel;

    @NotNull(message = "El tipo de vehículo es obligatorio (MOTO, AUTO, FURGONETA, CAMION)")
    private VehicleType vehicleType;

    @NotBlank(message = "El origen es obligatorio")
    @Size(min = 5, max = 300, message = "El origen debe tener entre 5 y 300 caracteres")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    @Size(min = 5, max = 300, message = "El destino debe tener entre 5 y 300 caracteres")
    private String destination;
}
