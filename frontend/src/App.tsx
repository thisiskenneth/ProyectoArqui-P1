import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import { Layout } from './Layout';
import TrackingPanel from './pages/TrackingPanel';
import OperatorPanel from './pages/OperatorPanel';
import ClientPanel from './pages/ClientPanel';
import FleetPanel from './pages/FleetPanel';
import Login from './pages/Login';
import { Package, Truck, Users, Search, LogOut } from 'lucide-react';
import { useState } from 'react';

const LandingPage = () => {
  const [trackingId, setTrackingId] = useState('');
  const navigate = useNavigate();

  const handleTrack = (e: React.FormEvent) => {
    e.preventDefault();
    if (trackingId.trim()) {
      navigate(`/tracking?id=${trackingId}`);
    }
  };

  const isLoggedIn = !!localStorage.getItem('token');

  return (
    <div style={{ textAlign: 'center', marginTop: '2rem' }}>
      {/* Hero Section */}
      <div style={{ padding: '4rem 2rem', background: 'rgba(0,0,0,0.2)', borderRadius: '16px', marginBottom: '3rem', border: '1px solid var(--glass-border)' }}>
        <h1 style={{ fontSize: '3.5rem', marginBottom: '1rem', background: 'linear-gradient(to right, #3b82f6, #8b5cf6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', fontWeight: 800 }}>
          LogiFlow Platform
        </h1>
        <p style={{ fontSize: '1.25rem', color: 'var(--text-secondary)', marginBottom: '2rem', maxWidth: '600px', margin: '0 auto 2rem auto' }}>
          El ecosistema logístico definitivo. Gestiona tu flota, opera rutas en vivo y mantén a tus clientes informados con precisión milimétrica.
        </p>

        <form onSubmit={handleTrack} style={{ display: 'flex', gap: '0.5rem', justifyContent: 'center', maxWidth: '500px', margin: '0 auto' }}>
          <input
            type="text"
            className="input-field"
            placeholder="Ingresa tu código de envío (Ej: SHP-9F2B)"
            value={trackingId}
            onChange={(e) => setTrackingId(e.target.value)}
            style={{ padding: '1rem', fontSize: '1.1rem' }}
          />
          <button type="submit" className="btn btn-primary" style={{ padding: '0 2rem' }}>
            <Search size={20} style={{ marginRight: '0.5rem' }} /> Rastrear
          </button>
        </form>
      </div>

      <h2 style={{ marginBottom: '1rem', color: 'var(--text-primary)' }}>Portales de Acceso</h2>

      {!isLoggedIn && (
        <div style={{ marginBottom: '2rem', padding: '1rem', background: 'rgba(234,179,8,0.1)', border: '1px solid var(--warning)', borderRadius: '8px' }}>
          Para ingresar a los paneles privados, debes{' '}
          <Link to="/login" style={{ color: 'var(--accent-primary)', fontWeight: 'bold' }}>Iniciar Sesión</Link>
        </div>
      )}

      {isLoggedIn && (
        <div style={{ marginBottom: '2rem' }}>
          <button
            onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}
            className="btn btn-secondary"
            style={{ padding: '0.5rem 1rem' }}
          >
            <LogOut size={16} style={{ marginRight: '0.5rem' }} /> Cerrar Sesión
          </button>
        </div>
      )}

      <div className="grid-layout grid-cols-2">
        <Link to="/client" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer', padding: '2rem' }}>
            <Package size={48} color="var(--success)" />
            <h2 style={{ margin: 0 }}>Soy Cliente</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center', margin: 0 }}>Historial de pedidos y facturas descargables.</p>
          </div>
        </Link>

        <Link to="/operator" style={{ textDecoration: 'none' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer', padding: '2rem' }}>
            <Users size={48} color="var(--accent-secondary)" />
            <h2 style={{ margin: 0 }}>Soy Operador</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center', margin: 0 }}>Crea pedidos, asigna vehículos manualmente y monitorea la operación.</p>
          </div>
        </Link>

        <Link to="/fleet" style={{ textDecoration: 'none', gridColumn: 'span 2' }}>
          <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', cursor: 'pointer', padding: '2rem' }}>
            <Truck size={48} color="var(--warning)" />
            <h2 style={{ margin: 0 }}>Administrar Flota</h2>
            <p style={{ color: 'var(--text-secondary)', textAlign: 'center', margin: 0 }}>Gestión de vehículos, conductores, estadísticas de la flota y mantenimiento en taller.</p>
          </div>
        </Link>
      </div>

      <footer style={{ marginTop: '4rem', padding: '2rem 0', borderTop: '1px solid var(--glass-border)', color: 'var(--text-secondary)' }}>
        <p>Cobertura: Local, Provincial y Nacional | Soporte: soporte@logiflow.com</p>
      </footer>
    </div>
  );
};

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<LandingPage />} />
        <Route path="login" element={<Login />} />
        <Route path="tracking" element={<TrackingPanel />} />
        <Route path="operator" element={<OperatorPanel />} />
        <Route path="client" element={<ClientPanel />} />
        <Route path="fleet" element={<FleetPanel />} />
      </Route>
    </Routes>
  );
}

export default App;
