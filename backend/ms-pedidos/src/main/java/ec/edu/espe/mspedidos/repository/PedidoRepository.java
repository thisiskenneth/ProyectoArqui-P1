package ec.edu.espe.mspedidos.repository;

import ec.edu.espe.mspedidos.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    
    Page<Pedido> findByClienteId(UUID clienteId, Pageable pageable);
}
