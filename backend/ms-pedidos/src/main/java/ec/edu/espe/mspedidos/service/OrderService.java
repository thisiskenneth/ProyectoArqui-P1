package ec.edu.espe.mspedidos.service;

import ec.edu.espe.mspedidos.dto.OrderRequest;
import ec.edu.espe.mspedidos.dto.OrderResponse;
import ec.edu.espe.mspedidos.entity.Order;
import ec.edu.espe.mspedidos.entity.OrderStatus;
import ec.edu.espe.mspedidos.publisher.OrderPublisher;
import ec.edu.espe.mspedidos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Pedido no encontrado con id: " + id));
        return convertToResponse(order);
    }

    public List<OrderResponse> findActiveByCliente(String clienteId) {
        return orderRepository.findByClienteIdAndStatus(clienteId, OrderStatus.CREATED).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse save(OrderRequest request) {
        // Regla de negocio: no duplicar pedidos del mismo cliente en estado CREATED
        boolean hasOpenOrder = !orderRepository
                .findByClienteIdAndStatus(request.getClienteId(), OrderStatus.CREATED).isEmpty();
        if (hasOpenOrder) {
            throw new IllegalStateException(
                "El cliente ya tiene un pedido activo. Debe finalizarlo o cancelarlo antes de crear uno nuevo.");
        }

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
                .status(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Pedido creado: {} para cliente: {}", savedOrder.getId(), savedOrder.getClienteId());

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("origin", savedOrder.getOrigin());
        event.put("destination", savedOrder.getDestination());
        event.put("weightKg", savedOrder.getWeightKg());
        event.put("geographicLevel", savedOrder.getGeographicLevel().name());
        event.put("vehicleType", savedOrder.getVehicleType().name());
        event.put("status", savedOrder.getStatus().name());
        orderPublisher.publishOrderCreated(event);

        return convertToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse cancel(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Pedido no encontrado con id: " + id));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("No se puede cancelar un pedido que ya fue entregado.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("El pedido ya está cancelado.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        log.info("Pedido cancelado: {}", savedOrder.getId());

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("status", savedOrder.getStatus().name());
        orderPublisher.publishOrderCancelled(event);

        return convertToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse markAsDelivered(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Pedido no encontrado con id: " + id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("No se puede marcar como entregado un pedido cancelado.");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("El pedido ya fue marcado como entregado.");
        }

        order.setStatus(OrderStatus.DELIVERED);
        Order savedOrder = orderRepository.save(order);
        log.info("Pedido entregado: {}", savedOrder.getId());

        Map<String, Object> event = new HashMap<>();
        event.put("orderId", savedOrder.getId().toString());
        event.put("customerEmail", savedOrder.getCustomerEmail());
        event.put("status", savedOrder.getStatus().name());
        event.put("weightKg", savedOrder.getWeightKg());
        event.put("geographicLevel", savedOrder.getGeographicLevel().name());
        event.put("vehicleType", savedOrder.getVehicleType().name());
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
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
