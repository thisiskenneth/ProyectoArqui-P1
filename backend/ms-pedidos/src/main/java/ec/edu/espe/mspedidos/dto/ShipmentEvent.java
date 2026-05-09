package ec.edu.espe.mspedidos.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShipmentEvent implements Serializable {
    private String shipmentId;
    private String orderId;
    private String driverName;
    private String plate;
    private String customerEmail;
}
