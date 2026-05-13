package ec.edu.espe.msruteo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AssignmentRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    @Pattern(
        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
        message = "El ID del pedido debe ser un UUID válido"
    )
    private String orderId;

    @NotBlank(message = "El email del cliente es obligatorio para las notificaciones")
    @Email(message = "El email debe tener formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String customerEmail;

    @NotBlank(message = "El origen es obligatorio")
    @Size(min = 5, max = 300, message = "El origen debe tener entre 5 y 300 caracteres")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    @Size(min = 5, max = 300, message = "El destino debe tener entre 5 y 300 caracteres")
    private String destination;
}
