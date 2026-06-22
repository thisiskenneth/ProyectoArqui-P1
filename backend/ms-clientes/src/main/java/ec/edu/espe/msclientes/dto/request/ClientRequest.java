package ec.edu.espe.msclientes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ClientRequest {
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String phone;
    private String address;

    private UUID corporateAccountId;

    /**
     * Opcional. Si se envia, al crear el cliente se provisiona su usuario de acceso
     * (rol CLIENT) en ms-auth mediante el evento cliente.creado. El login es el email.
     */
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;
}
