package ec.edu.espe.mspedidos.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
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
    private LocalDateTime createdAt;
}
