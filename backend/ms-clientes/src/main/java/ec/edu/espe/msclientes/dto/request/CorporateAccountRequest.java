package ec.edu.espe.msclientes.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CorporateAccountRequest {

    /**
     * RUC ecuatoriano: 13 dígitos.
     * Los primeros 10 dígitos corresponden a la cédula (persona natural) o código (empresa).
     * Los últimos 3 dígitos son '001'.
     */
    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(
        regexp = "^[0-9]{13}$",
        message = "El RUC debe contener exactamente 13 dígitos numéricos"
    )
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 3, max = 200, message = "La razón social debe tener entre 3 y 200 caracteres")
    private String businessName;

    @NotNull(message = "El límite de crédito es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El límite de crédito no puede ser negativo")
    @DecimalMax(value = "9999999.99", message = "El límite de crédito no puede superar 9,999,999.99")
    @Digits(integer = 7, fraction = 2, message = "El límite de crédito debe tener máximo 7 enteros y 2 decimales")
    private BigDecimal creditLimit;

    @Size(max = 100, message = "La industria no puede superar los 100 caracteres")
    private String industry;
}
