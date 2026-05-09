package ec.edu.espe.msnotificaciones.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String recipient;
    private String subject;
    private String body;
    private String type; // EMAIL, SMS, PUSH
}
