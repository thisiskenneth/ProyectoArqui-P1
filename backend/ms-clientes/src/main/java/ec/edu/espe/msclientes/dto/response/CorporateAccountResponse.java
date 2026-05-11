package ec.edu.espe.msclientes.dto.response;

import lombok.Data;

@Data
public class CorporateAccountResponse {
    private Long id;
    private String ruc;
    private String businessName;
    private Double creditLimit;
    private String industry;
}
