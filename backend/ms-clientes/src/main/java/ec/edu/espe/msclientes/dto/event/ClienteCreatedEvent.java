package ec.edu.espe.msclientes.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Evento de dominio publicado cuando se da de alta un cliente.
 * ms-auth lo consume para provisionar el usuario de acceso (rol CLIENT).
 * El password viaja solo para el provisionamiento; no se persiste en el contexto de Clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreatedEvent {
    private UUID clientId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
