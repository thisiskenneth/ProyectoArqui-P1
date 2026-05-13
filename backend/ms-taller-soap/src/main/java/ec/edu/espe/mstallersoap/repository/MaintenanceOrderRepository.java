package ec.edu.espe.mstallersoap.repository;

import ec.edu.espe.mstallersoap.entity.MaintenanceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, UUID> {

    Optional<MaintenanceOrder> findFirstByMatriculaOrderByFechaIngresoDesc(String matricula);
}
