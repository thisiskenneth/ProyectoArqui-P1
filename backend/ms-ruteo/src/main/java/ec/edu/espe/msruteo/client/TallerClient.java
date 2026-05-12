package ec.edu.espe.msruteo.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class TallerClient {

    private final RestTemplate restTemplate;

    @Value("${taller.service.url}")
    private String tallerUrl;

    public void requestMaintenance(String matricula, String descripcion) {
        try {
            MaintenanceRequest request = new MaintenanceRequest();
            request.setMatricula(matricula);
            request.setDescripcion(descripcion);

            log.info("Simulando envío de orden de mantenimiento a ms-taller para el vehículo {}", matricula);
            ResponseEntity<String> response = restTemplate.postForEntity(tallerUrl + "/orders", request, String.class);
            log.info("Respuesta de ms-taller: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error al solicitar mantenimiento en ms-taller: {}", e.getMessage());
        }
    }

    @Data
    public static class MaintenanceRequest {
        private String matricula;
        private String descripcion;
    }
}
