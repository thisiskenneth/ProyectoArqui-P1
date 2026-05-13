package ec.edu.espe.msflotarest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverDto {
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @NotBlank(message = "La cedula es obligatoria")
    @Pattern(regexp = "^[0-9]{10}$", message = "La cedula debe tener 10 digitos")
    private String licenseNumber;

    @Pattern(regexp = "^(\\+?[0-9\\-\\s]{7,20})?$", message = "Formato de telefono invalido")
    private String phone;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean available;

    @JsonIgnore
    @AssertTrue(message = "La cedula ecuatoriana no es valida")
    public boolean isLicenseNumberCedulaValida() {
        if (licenseNumber == null || !licenseNumber.matches("^[0-9]{10}$")) {
            return true;
        }

        int province = Integer.parseInt(licenseNumber.substring(0, 2));
        int thirdDigit = Character.getNumericValue(licenseNumber.charAt(2));
        if (province < 1 || province > 24 || thirdDigit >= 6) {
            return false;
        }

        int[] coefficients = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int sum = 0;
        for (int i = 0; i < coefficients.length; i++) {
            int value = Character.getNumericValue(licenseNumber.charAt(i)) * coefficients[i];
            sum += value > 9 ? value - 9 : value;
        }

        int verifier = (10 - (sum % 10)) % 10;
        return verifier == Character.getNumericValue(licenseNumber.charAt(9));
    }
}
