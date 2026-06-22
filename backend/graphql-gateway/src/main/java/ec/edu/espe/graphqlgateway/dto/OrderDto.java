package ec.edu.espe.graphqlgateway.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDto {
    private String id;
    private String clienteId;
    private String customerEmail;
    private List<String> items;
    private Double total;
    private Double weightKg;
    private String geographicLevel;
    private String vehicleType;
    private String origin;
    private String destination;
    private String status;
}
