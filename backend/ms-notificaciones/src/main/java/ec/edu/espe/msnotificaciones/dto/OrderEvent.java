package ec.edu.espe.msnotificaciones.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvent implements Serializable {
    private String orderId;
    private String customerEmail;
    private String status; // CREATED, DELIVERED
}
