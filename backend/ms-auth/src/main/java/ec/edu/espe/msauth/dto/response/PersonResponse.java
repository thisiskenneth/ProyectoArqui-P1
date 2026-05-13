package ec.edu.espe.msauth.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PersonResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String address;
    private String email;
    private String documentNumber;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
