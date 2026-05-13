package ec.edu.espe.mstallersoap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MantenimientoRequest {

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(min = 2, max = 20, message = "La matrícula debe tener entre 2 y 20 caracteres")
    private String matricula;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, max = 500, message = "La descripción debe tener entre 3 y 500 caracteres")
    private String descripcion;
}
