package ec.edu.espe.msclientes.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClientResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private UUID corporateAccountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
