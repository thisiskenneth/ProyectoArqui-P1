package ec.edu.espe.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Copia local del evento publicado por ms-clientes. Solo se usa para provisionar
 * el usuario de acceso (rol CLIENT). Mismos nombres de campo que el productor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreatedEvent {
    private UUID clientId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
