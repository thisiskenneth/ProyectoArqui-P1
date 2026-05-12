import { Link, Outlet, useLocation } from 'react-router-dom';
import { Package, Truck, Users, Map as MapIcon, Home } from 'lucide-react';

export const Layout = () => {
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path ? 'active' : '';

  return (
    <div className="app-container">
      <nav className="navbar">
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '1.25rem', fontWeight: '700', color: 'var(--text-primary)' }}>
          <Package className="text-accent" color="var(--accent-primary)"/> LogiFlow
        </div>
        <div className="nav-links">
          <Link to="/" className={`nav-link ${isActive('/')}`}><Home size={18}/> Inicio</Link>
          <Link to="/tracking" className={`nav-link ${isActive('/tracking')}`}><MapIcon size={18}/> Seguimiento Público</Link>
          <Link to="/operator" className={`nav-link ${isActive('/operator')}`}><Users size={18}/> Operador</Link>
          <Link to="/client" className={`nav-link ${isActive('/client')}`}><Package size={18}/> Cliente</Link>
          <Link to="/fleet" className={`nav-link ${isActive('/fleet')}`}><Truck size={18}/> Flota y Taller</Link>
        </div>
      </nav>
      
      <main style={{ marginTop: '2rem' }}>
        <Outlet />
      </main>
    </div>
  );
};
