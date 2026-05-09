package ec.edu.espe.msflotarest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverDto {
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "License number is mandatory")
    private String licenseNumber;

    private String phone;

    @NotNull(message = "Availability status is mandatory")
    private Boolean available;
}
