package ec.edu.espe.graphqlgateway.client;

import ec.edu.espe.graphqlgateway.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderClient {

    private final RestTemplate restTemplate;

    @Value("${services.orders.url}")
    private String orderUrl;

    public List<OrderDto> getActiveOrders(String clienteId) {
        return restTemplate.exchange(
                orderUrl + "/active/client/" + clienteId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDto>>() {}
        ).getBody();
    }

    public OrderDto createOrder(OrderDto order) {
        return restTemplate.postForObject(orderUrl, order, OrderDto.class);
    }

    public OrderDto cancelOrder(String id) {
        return restTemplate.postForObject(orderUrl + "/" + id + "/cancel", null, OrderDto.class);
    }
}
