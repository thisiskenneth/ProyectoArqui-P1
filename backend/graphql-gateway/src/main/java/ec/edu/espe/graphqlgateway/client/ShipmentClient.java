package ec.edu.espe.graphqlgateway.client;

import ec.edu.espe.graphqlgateway.dto.ShipmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ShipmentClient {

    private final RestTemplate restTemplate;

    @Value("${services.shipments.url}")
    private String shipmentUrl;

    public ShipmentDto getShipment(String id) {
        return restTemplate.getForObject(shipmentUrl + "/" + id, ShipmentDto.class);
    }
}
