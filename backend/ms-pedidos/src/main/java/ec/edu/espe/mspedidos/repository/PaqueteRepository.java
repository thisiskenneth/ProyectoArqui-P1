package ec.edu.espe.mspedidos.repository;

import ec.edu.espe.mspedidos.entity.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, UUID> {
    
    List<Paquete> findByPedidoId(UUID pedidoId);
}
