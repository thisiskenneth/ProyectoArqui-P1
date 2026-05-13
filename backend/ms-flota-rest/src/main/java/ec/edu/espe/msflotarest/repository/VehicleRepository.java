package ec.edu.espe.msflotarest.repository;

import ec.edu.espe.msflotarest.entity.Vehicle;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlateIgnoreCase(String plate);
    boolean existsByPlateIgnoreCase(String plate);
    List<Vehicle> findByStatus(VehicleStatus status);
}
