import { Routes, Route, Link } from 'react-router-dom';
import { Layout } from './Layout';
import TrackingPanel from './pages/TrackingPanel';
import OperatorPanel from './pages/OperatorPanel';
import ClientPanel from './pages/ClientPanel';
import FleetPanel from './pages/FleetPanel';
import { Package, Truck, Users, Map as MapIcon } from 'lucide-react';

const LandingPage = () => {
  return (
    <div style={{ textAlign: 'center', marginTop: '4rem' }}>
      <h1 style={{ fontSize: '3rem', marginBottom: '1rem', background: 'linear-gradient(to right, #3b82f6, #8b5cf6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
        Plataforma LogiFlow
      </h1>
      <p style={{ fontSize: '1.25rem', color: 'var(--text-secondary)', marginBottom: '3rem' }}>
        Sistema avanzado de logística y ruteo en tiempo real.
      </p>

      <div className="grid-layout grid-cols-2">
        <Link to="/tracking" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer' }}>
            <MapIcon size={48} color="var(--accent-primary)" />
            <h2>Seguimiento Público</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center' }}>Sigue tu envío en tiempo real vía WebSocket y Mapas interactivos.</p>
          </div>
        </Link>
        
        <Link to="/operator" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer' }}>
            <Users size={48} color="var(--accent-secondary)" />
            <h2>Panel de Operador</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center' }}>Asigna vehículos, crea pedidos (GraphQL) y observa rutas en vivo.</p>
          </div>
        </Link>

        <Link to="/client" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer' }}>
            <Package size={48} color="var(--success)" />
            <h2>Panel de Cliente</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center' }}>Revisa el historial de tus pedidos y consulta facturas generadas.</p>
          </div>
        </Link>

        <Link to="/fleet" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer' }}>
            <Truck size={48} color="var(--warning)" />
            <h2>Flota y Mantenimiento</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center' }}>Gestiona vehículos, conductores y solicita órdenes al Taller.</p>
          </div>
        </Link>
      </div>
    </div>
  );
};

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<LandingPage />} />
        <Route path="tracking" element={<TrackingPanel />} />
        <Route path="operator" element={<OperatorPanel />} />
        <Route path="client" element={<ClientPanel />} />
        <Route path="fleet" element={<FleetPanel />} />
      </Route>
    </Routes>
  );
}

export default App;
