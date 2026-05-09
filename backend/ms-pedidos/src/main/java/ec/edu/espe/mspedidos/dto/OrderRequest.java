package ec.edu.espe.mspedidos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Client ID is mandatory")
    private String clienteId;

    @Email(message = "Customer email should be valid")
    @NotBlank(message = "Customer email is mandatory")
    private String customerEmail;

    @NotNull(message = "Items list cannot be empty")
    private List<String> items;

    @Positive(message = "Total must be positive")
    private Double total;

    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @NotBlank(message = "Geographic level is mandatory (LOCAL, PROVINCIAL, NATIONAL)")
    private String geographicLevel;

    @NotBlank(message = "Vehicle type is mandatory (LIGHT, HEAVY, etc.)")
    private String vehicleType;

    @NotBlank(message = "Origin is mandatory")
    private String origin;

    @NotBlank(message = "Destination is mandatory")
    private String destination;
}
