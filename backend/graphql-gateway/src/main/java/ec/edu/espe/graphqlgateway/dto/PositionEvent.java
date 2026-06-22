package ec.edu.espe.graphqlgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEvent implements Serializable {
    private String shipmentId;
    private String orderId;
    private Double lat;
    private Double lng;
    private Double speed;
    private String eta;
    private String timestamp;
}
