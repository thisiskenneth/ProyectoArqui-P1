package ec.edu.espe.msruteo.repository;

import ec.edu.espe.msruteo.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    List<Shipment> findByOrderId(UUID orderId);
    Optional<Shipment> findFirstByOrderId(UUID orderId);
    boolean existsByOrderId(UUID orderId);
}
