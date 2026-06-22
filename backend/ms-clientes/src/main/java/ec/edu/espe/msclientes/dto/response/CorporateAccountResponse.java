package ec.edu.espe.msclientes.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CorporateAccountResponse {
    private UUID id;
    private String ruc;
    private String businessName;
    private Double creditLimit;
    private String industry;
}
