package ec.edu.espe.msclientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CorporateAccountRequest {
    @NotBlank(message = "RUC is mandatory")
    private String ruc;

    @NotBlank(message = "Business name is mandatory")
    private String businessName;

    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;

    private String industry;
}
