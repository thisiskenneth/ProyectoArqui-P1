import { useState, useEffect } from 'react';
import { api } from '../lib/api';
import { Truck, Wrench } from 'lucide-react';

export default function FleetPanel() {
  const [vehicles, setVehicles] = useState<any[]>([]);
  const [loadingVehicles, setLoadingVehicles] = useState(false);
  
  const [plate, setPlate] = useState('');
  const [description, setDescription] = useState('');
  const [maintenanceStatus, setMaintenanceStatus] = useState('');

  // Form for new vehicle
  const [newPlate, setNewPlate] = useState('');
  const [newBrand, setNewBrand] = useState('');
  const [newModel, setNewModel] = useState('');
  const [newType, setNewType] = useState('CAMION_LIGERO');

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    setLoadingVehicles(true);
    try {
      const res = await api.get('/vehicles');
      setVehicles(res.data);
    } catch (err) {
      console.error('Error fetching vehicles', err);
    } finally {
      setLoadingVehicles(false);
    }
  };

  const handleRegisterMaintenance = async (e: React.FormEvent) => {
    e.preventDefault();
    setMaintenanceStatus('Enviando...');
    try {
      const res = await api.post('/maintenance/orders', {
        matricula: plate,
        descripcion: description
      });
      setMaintenanceStatus(`Éxito: ${res.data.mensaje} (Orden: ${res.data.codigoOrden})`);
      setPlate('');
      setDescription('');
    } catch (err) {
      setMaintenanceStatus('Error al conectar con ms-taller a través del Gateway.');
    }
  };

  const handleCreateVehicle = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/vehicles', {
        plate: newPlate,
        brand: newBrand,
        model: newModel,
        type: newType,
        capacityKg: 5000,
        autonomyKm: 800,
        status: 'AVAILABLE'
      });
      setNewPlate(''); setNewBrand(''); setNewModel('');
      fetchVehicles();
    } catch (err) {
      console.error('Error creating vehicle', err);
    }
  };

  const handleDeleteVehicle = async (id: number) => {
    try {
      await api.delete(`/vehicles/${id}`);
      fetchVehicles();
    } catch (err) {
      console.error('Error deleting vehicle', err);
    }
  };

  return (
    <div className="grid-layout grid-cols-2">
      {/* Flota Actual */}
      <div className="glass-panel">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', margin: 0 }}>
            <Truck color="var(--accent-primary)"/> Gestión de Flota
          </h2>
          <button onClick={fetchVehicles} className="btn btn-secondary" style={{ padding: '0.5rem' }}>Actualizar</button>
        </div>

        {loadingVehicles ? <p>Cargando vehículos...</p> : (
          <div className="grid-layout" style={{ gap: '1rem' }}>
            {vehicles.map((v: any) => (
              <div key={v.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem', background: 'rgba(0,0,0,0.2)', borderRadius: '8px' }}>
                <div>
                  <strong>{v.plate}</strong>
                  <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{v.brand} {v.model} ({v.type})</div>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <span className={`badge ${v.status === 'AVAILABLE' ? 'badge-success' : 'badge-warning'}`}>
                    {v.status}
                  </span>
                  <button onClick={() => handleDeleteVehicle(v.id)} className="btn btn-secondary" style={{ padding: '0.25rem 0.5rem', color: 'var(--danger)', borderColor: 'var(--danger)' }}>X</button>
                </div>
              </div>
            ))}
            {vehicles.length === 0 && <p>No hay vehículos registrados.</p>}
          </div>
        )}

        <h3 style={{ marginTop: '2rem', marginBottom: '1rem' }}>Registrar Nuevo Vehículo</h3>
        <form onSubmit={handleCreateVehicle} className="grid-layout grid-cols-2" style={{ gap: '1rem' }}>
          <input type="text" placeholder="Matrícula" required className="input-field" value={newPlate} onChange={e => setNewPlate(e.target.value)} />
          <input type="text" placeholder="Marca" required className="input-field" value={newBrand} onChange={e => setNewBrand(e.target.value)} />
          <input type="text" placeholder="Modelo" required className="input-field" value={newModel} onChange={e => setNewModel(e.target.value)} />
          <select className="input-field" value={newType} onChange={e => setNewType(e.target.value)}>
            <option value="CAMION_LIGERO">Camión Ligero</option>
            <option value="CAMION_PESADO">Camión Pesado</option>
            <option value="FURGONETA">Furgoneta</option>
          </select>
          <button type="submit" className="btn btn-primary" style={{ gridColumn: 'span 2' }}>Agregar Vehículo</button>
        </form>
      </div>

      {/* Mantenimiento */}
      <div className="glass-panel">
        <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
          <Wrench color="var(--warning)"/> Solicitar Mantenimiento
        </h2>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem' }}>
          Esta acción invoca internamente el endpoint REST de <code>ms-taller</code> (simulación de integración de Fase 3).
        </p>

        {maintenanceStatus && (
          <div className={`badge ${maintenanceStatus.includes('Error') ? 'badge-danger' : 'badge-success'}`} style={{ marginBottom: '1rem', display: 'inline-block' }}>
            {maintenanceStatus}
          </div>
        )}

        <form onSubmit={handleRegisterMaintenance} className="grid-layout" style={{ gap: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Matrícula</label>
            <input 
              type="text" 
              required 
              className="input-field" 
              placeholder="Ej: ABC-1234"
              value={plate} 
              onChange={e => setPlate(e.target.value)} 
            />
          </div>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Descripción del Problema</label>
            <textarea 
              required 
              className="input-field" 
              rows={4}
              placeholder="Falla en frenos, cambio de aceite..."
              value={description} 
              onChange={e => setDescription(e.target.value)} 
            />
          </div>
          
          <button type="submit" className="btn btn-primary" style={{ marginTop: '0.5rem' }}>
            Enviar Orden al Taller
          </button>
        </form>
      </div>
    </div>
  );
}
