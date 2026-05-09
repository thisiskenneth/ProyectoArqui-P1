package ec.edu.espe.graphqlgateway.service;

import ec.edu.espe.graphqlgateway.dto.PositionEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TrackingPositionStore {

    private final ConcurrentMap<String, PositionEvent> latestByShipmentId = new ConcurrentHashMap<>();

    public void save(PositionEvent event) {
        if (event.getShipmentId() != null) {
            latestByShipmentId.put(event.getShipmentId(), event);
        }
    }

    public Optional<PositionEvent> findLatest(String shipmentId) {
        return Optional.ofNullable(latestByShipmentId.get(shipmentId));
    }
}
