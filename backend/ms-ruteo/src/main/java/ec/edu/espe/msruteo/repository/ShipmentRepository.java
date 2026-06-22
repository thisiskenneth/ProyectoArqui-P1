package ec.edu.espe.msruteo.repository;

import ec.edu.espe.msruteo.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByOrderId(String orderId);
    boolean existsByOrderId(String orderId);
}
