package ec.edu.espe.msflotarest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MaintenanceOrderRequest {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    private String descripcion;
}
