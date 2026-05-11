package ec.edu.espe.msflotarest.dto.response;

import lombok.Data;

@Data
public class DriverResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phone;
    private Boolean available;
}
