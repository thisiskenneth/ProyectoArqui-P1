import { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import { createStompClient } from '../lib/socket';
import { Search, Navigation } from 'lucide-react';
import { Client } from '@stomp/stompjs';

// Custom icon for truck
const truckIcon = new L.Icon({
  iconUrl: 'https://cdn-icons-png.flaticon.com/512/709/709790.png',
  iconSize: [38, 38],
  iconAnchor: [19, 38],
  popupAnchor: [0, -38]
});

// Component to dynamically update map center
const MapUpdater = ({ center }: { center: [number, number] }) => {
  const map = useMap();
  useEffect(() => {
    map.setView(center, map.getZoom());
  }, [center, map]);
  return null;
};

export default function TrackingPanel() {
  const [shipmentId, setShipmentId] = useState('');
  const [isTracking, setIsTracking] = useState(false);
  const [position, setPosition] = useState<[number, number] | null>(null);
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [lastUpdate, setLastUpdate] = useState<string>('');

  const startTracking = (e: React.FormEvent) => {
    e.preventDefault();
    if (!shipmentId) return;

    setIsTracking(true);
    
    // Connect WebSocket
    const client = createStompClient();
    
    client.onConnect = () => {
      console.log('Connected to WS');
      client.subscribe(`/topic/shipment/${shipmentId}`, (message) => {
        const payload = JSON.parse(message.body);
        if (payload.latitude && payload.longitude) {
          setPosition([payload.latitude, payload.longitude]);
          setLastUpdate(new Date().toLocaleTimeString());
        }
      });
    };

    client.activate();
    setStompClient(client);
    
    // Simulate initial position if none comes immediately
    setPosition([-0.180653, -78.467834]); // Quito center
  };

  useEffect(() => {
    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, [stompClient]);

  return (
    <div className="grid-layout" style={{ gridTemplateColumns: '1fr', gap: '2rem' }}>
      <div className="glass-panel">
        <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1rem' }}>
          <Navigation color="var(--accent-primary)"/> Seguimiento Público
        </h2>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem' }}>
          Ingresa el ID de tu envío para ver su ubicación en tiempo real.
        </p>

        <form onSubmit={startTracking} style={{ display: 'flex', gap: '1rem' }}>
          <input 
            type="text" 
            className="input-field" 
            placeholder="Ej: SHP-9F2B..." 
            value={shipmentId}
            onChange={(e) => setShipmentId(e.target.value)}
            disabled={isTracking}
          />
          <button type="submit" className="btn btn-primary" disabled={isTracking || !shipmentId}>
            <Search size={18}/> Buscar
          </button>
        </form>
      </div>

      {isTracking && (
        <div className="glass-panel" style={{ padding: 0, overflow: 'hidden' }}>
          <div style={{ padding: '1rem 1.5rem', borderBottom: '1px solid var(--glass-border)', display: 'flex', justifyContent: 'space-between' }}>
            <h3 style={{ margin: 0 }}>Ruta en Vivo: {shipmentId}</h3>
            {lastUpdate && <span className="badge badge-success">Última actualización: {lastUpdate}</span>}
          </div>
          
          <div className="map-container" style={{ borderRadius: 0, border: 'none' }}>
            <MapContainer 
              center={position || [-0.180653, -78.467834]} 
              zoom={13} 
              style={{ height: '100%', width: '100%' }}
            >
              <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
              />
              {position && (
                <>
                  <MapUpdater center={position} />
                  <Marker position={position} icon={truckIcon}>
                    <Popup>
                      Tu envío está aquí.
                    </Popup>
                  </Marker>
                </>
              )}
            </MapContainer>
          </div>
        </div>
      )}
    </div>
  );
}
