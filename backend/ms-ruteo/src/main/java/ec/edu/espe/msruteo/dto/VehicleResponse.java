package ec.edu.espe.msruteo.dto;

import lombok.Data;

@Data
public class VehicleResponse {
    private String id;
    private String plate;
    private String type;
    private String status;
}
