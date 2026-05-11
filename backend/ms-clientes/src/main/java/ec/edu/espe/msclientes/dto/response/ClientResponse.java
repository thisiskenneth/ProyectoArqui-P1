package ec.edu.espe.msclientes.dto.response;

import lombok.Data;

@Data
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Long corporateAccountId;
}
