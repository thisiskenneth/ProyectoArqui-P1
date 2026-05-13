package ec.edu.espe.mspedidos.repository;

import ec.edu.espe.mspedidos.entity.Order;
import ec.edu.espe.mspedidos.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByClienteIdAndStatus(String clienteId, OrderStatus status);
    boolean existsByClienteIdAndStatus(String clienteId, OrderStatus status);
}
