package ec.edu.espe.msclientes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CorporateAccountRequest {
    @NotBlank(message = "RUC is mandatory")
    @Pattern(regexp = "^[0-9]{13}$", message = "El RUC debe tener exactamente 13 digitos")
    private String ruc;

    @NotBlank(message = "Business name is mandatory")
    private String businessName;

    @NotNull(message = "Credit limit is mandatory")
    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;

    private String industry;
}
