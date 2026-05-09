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
public class ShipmentEvent implements Serializable {
    private String shipmentId;
    private String orderId;
    private String driverName;
    private String plate;
    private String customerEmail;
}
