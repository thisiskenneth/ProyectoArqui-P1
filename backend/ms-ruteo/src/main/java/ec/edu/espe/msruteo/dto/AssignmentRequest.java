package ec.edu.espe.msruteo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignmentRequest {

    @NotBlank(message = "El orderId es obligatorio")
    private String orderId;

    @Email(message = "El correo del cliente debe ser valido")
    @NotBlank(message = "El correo del cliente es obligatorio")
    private String customerEmail;

    @NotBlank(message = "El origen es obligatorio")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    private String destination;
}
