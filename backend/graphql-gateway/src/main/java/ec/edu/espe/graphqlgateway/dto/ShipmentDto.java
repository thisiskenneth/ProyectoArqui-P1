package ec.edu.espe.graphqlgateway.dto;

import lombok.Data;

@Data
public class ShipmentDto {
    private String id;
    private String orderId;
    private String vehiclePlate;
    private String status;
    private String origin;
    private String destination;
}
