package ec.edu.espe.mspedidos.dto;

import ec.edu.espe.mspedidos.entity.GeographicLevel;
import ec.edu.espe.mspedidos.entity.OrderStatus;
import ec.edu.espe.mspedidos.entity.VehicleType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private String clienteId;
    private String customerEmail;
    private List<String> items;
    private BigDecimal total;
    private Double weightKg;
    private GeographicLevel geographicLevel;
    private VehicleType vehicleType;
    private String origin;
    private String destination;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
