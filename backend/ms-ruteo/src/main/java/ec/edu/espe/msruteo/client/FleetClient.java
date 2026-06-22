package ec.edu.espe.msruteo.client;

import ec.edu.espe.msruteo.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FleetClient {

    private final RestTemplate restTemplate;

    @Value("${fleet.service.url}")
    private String fleetUrl;

    public List<VehicleResponse> getAvailableVehicles() {
        ResponseEntity<List<VehicleResponse>> response = restTemplate.exchange(
                fleetUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleResponse>>() {}
        );
        return response.getBody();
    }
}
