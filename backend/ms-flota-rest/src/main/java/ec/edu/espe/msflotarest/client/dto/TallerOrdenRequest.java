package ec.edu.espe.msflotarest.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallerOrdenRequest {
    private String matricula;
    private String descripcion;
}
