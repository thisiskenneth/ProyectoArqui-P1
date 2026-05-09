package ec.edu.espe.mspedidos.service;

import ec.edu.espe.mspedidos.dto.OrderRequest;
import ec.edu.espe.mspedidos.dto.OrderResponse;
import ec.edu.espe.mspedidos.entity.Order;
import ec.edu.espe.mspedidos.publisher.OrderPublisher;
import ec.edu.espe.mspedidos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToResponse(order);
    }

    public List<OrderResponse> findActiveByCliente(String clienteId) {
        return orderRepository.findByClienteIdAndStatus(clienteId, "CREATED").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse save(OrderRequest request) {
        Order order = Order.builder()
                .clienteId(request.getClienteId())
                .customerEmail(request.getCustomerEmail())
                .items(request.getItems())
                .total(request.getTotal())
                .weightKg(request.getWeightKg())
                .geographicLevel(request.getGeographicLevel())
                .vehicleType(request.getVehicleType())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Publish event
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("origin", savedOrder.getOrigin());
        event.put("destination", savedOrder.getDestination());
        event.put("weightKg", savedOrder.getWeightKg());
        event.put("geographicLevel", savedOrder.getGeographicLevel());
        event.put("vehicleType", savedOrder.getVehicleType());
        event.put("status", savedOrder.getStatus());
        orderPublisher.publishOrderCreated(event);

        return convertToResponse(savedOrder);
    }

    public OrderResponse cancel(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus("CANCELLED");
        Order savedOrder = orderRepository.save(order);

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("status", savedOrder.getStatus());
        orderPublisher.publishOrderCancelled(event);

        return convertToResponse(savedOrder);
    }

    public OrderResponse markAsDelivered(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("DELIVERED");
        Order savedOrder = orderRepository.save(order);

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("status", savedOrder.getStatus());
        event.put("weightKg", savedOrder.getWeightKg());
        event.put("geographicLevel", savedOrder.getGeographicLevel());
        event.put("vehicleType", savedOrder.getVehicleType());
        
        orderPublisher.publishOrderDelivered(event);

        return convertToResponse(savedOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setClienteId(order.getClienteId());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setItems(order.getItems());
        response.setTotal(order.getTotal());
        response.setWeightKg(order.getWeightKg());
        response.setGeographicLevel(order.getGeographicLevel());
        response.setVehicleType(order.getVehicleType());
        response.setOrigin(order.getOrigin());
        response.setDestination(order.getDestination());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}
