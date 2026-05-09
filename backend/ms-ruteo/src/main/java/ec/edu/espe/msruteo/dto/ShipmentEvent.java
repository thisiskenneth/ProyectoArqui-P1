package ec.edu.espe.msruteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentEvent implements Serializable {
    private String shipmentId;
    private String orderId;
    private String driverName;
    private String plate;
    private String customerEmail;
}
