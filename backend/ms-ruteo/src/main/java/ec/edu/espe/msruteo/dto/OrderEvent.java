package ec.edu.espe.msruteo.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderEvent implements Serializable {
    private String orderId;
    private String customerEmail;
    private String origin;
    private String destination;
    private String status;
    private Double weightKg;
    private String geographicLevel;
    private String vehicleType;
}
