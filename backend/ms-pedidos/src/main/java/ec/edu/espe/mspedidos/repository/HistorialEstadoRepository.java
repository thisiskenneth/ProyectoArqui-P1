package ec.edu.espe.mspedidos.repository;

import ec.edu.espe.mspedidos.entity.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, UUID> {

    List<HistorialEstado> findByPedidoIdOrderByFechaDesc(UUID pedidoId);
}
