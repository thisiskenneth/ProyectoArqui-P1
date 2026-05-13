package ec.edu.espe.msauth.dto;

import lombok.Data;

/**
 * @deprecated Reemplazado por UserCreateRequest (dto/request/UserCreateRequest.java).
 * La contraseña ahora se genera automáticamente desde el documentNumber (cédula).
 * Este archivo se mantiene solo para compatibilidad con código legado.
 */
@Data
@Deprecated
public class RegisterRequest {
    private String username;
    private String email;
    private String documentNumber;
    private String roleName;
}
