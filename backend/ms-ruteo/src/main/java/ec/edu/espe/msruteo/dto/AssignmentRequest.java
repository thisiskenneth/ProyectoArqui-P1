package ec.edu.espe.msruteo.dto;

import lombok.Data;

@Data
public class AssignmentRequest {
    private String orderId;
    private String customerEmail;
    private String origin;
    private String destination;
}
