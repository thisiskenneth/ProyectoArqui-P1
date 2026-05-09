package ec.edu.espe.msfacturacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvent implements Serializable {
    private String orderId;
    private String customerEmail;
    private String status;
    
    // Additional fields for billing calculation
    private Double weightKg;
    private String geographicLevel; // LOCAL, PROVINCIAL, NATIONAL
    private String vehicleType;
}
