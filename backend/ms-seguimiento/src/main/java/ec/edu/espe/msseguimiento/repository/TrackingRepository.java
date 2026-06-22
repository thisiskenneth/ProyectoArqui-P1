package ec.edu.espe.msseguimiento.repository;

import ec.edu.espe.msseguimiento.entity.TrackingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<TrackingLog, Long> {
    List<TrackingLog> findByTrackingNumberOrderByTimestampDesc(String trackingNumber);
}
