package ec.edu.espe.msflotarest.repository;

import ec.edu.espe.msflotarest.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findByAvailableTrue();
    boolean existsByLicenseNumber(String licenseNumber);
}
