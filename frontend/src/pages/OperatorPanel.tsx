import { useState, useEffect } from 'react';
import { api } from '../lib/api';
import { PackagePlus, Truck, RefreshCw } from 'lucide-react';

export default function OperatorPanel() {
  const [email, setEmail] = useState('');
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [weight, setWeight] = useState('');
  
  // REST state
  const [creating, setCreating] = useState(false);
  const [loading, setLoading] = useState(false);
  const [pedidos, setPedidos] = useState<any[]>([]);
  const [vehicles, setVehicles] = useState<any[]>([]);
  const [assigning, setAssigning] = useState(false);
  const [message, setMessage] = useState('');
  const [selectedOrderForAssign, setSelectedOrderForAssign] = useState<string | null>(null);

  useEffect(() => {
    fetchPedidos();
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    try {
      const res = await api.get('/vehicles');
      setVehicles(res.data.filter((v: any) => v.status === 'AVAILABLE'));
    } catch (err) {
      console.error('Error fetching vehicles', err);
    }
  };

  const fetchPedidos = async () => {
    setLoading(true);
    try {
      const res = await api.get('/gateway/pedidos?clienteId=admin');
      setPedidos(res.data);
    } catch (err) {
      console.error('Error fetching pedidos', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    setCreating(true);
    try {
      await api.post('/gateway/pedidos', {
        clienteId: 'admin',
        customerEmail: email,
        items: ['Paquete Estandar'],
        total: 50.0,
        origin,
        destination,
        weightKg: parseFloat(weight),
        geographicLevel: 'LOCAL',
        vehicleType: 'LIGHT'
      });
      setMessage('Pedido creado exitosamente (REST API)');
      fetchPedidos();
      setEmail(''); setOrigin(''); setDestination(''); setWeight('');
    } catch (err) {
      setMessage('Error al crear pedido (Revisa si el REST Gateway está vivo)');
    } finally {
      setCreating(false);
    }
  };

  const handleAssignVehicle = async (orderId: string, vehicleId: number) => {
    setAssigning(true);
    try {
      await api.post('/shipments/assign', {
        orderId,
        vehiculoId: vehicleId.toString(),
        conductorId: "1", // Hardcoded o proveniente del vehículo
        origin: 'Bodega Central',
        destination: 'Cliente Final',
        customerEmail: 'admin@logiflow.com'
      });
      setMessage(`Vehículo asignado al pedido ${orderId} (REST)`);
      setSelectedOrderForAssign(null);
      fetchPedidos();
      fetchVehicles();
    } catch (err) {
      setMessage('Error al asignar vehículo (REST)');
    } finally {
      setAssigning(false);
    }
  };

  return (
    <div className="grid-layout grid-cols-2">
      {/* Columna 1: Crear Pedido */}
      <div className="glass-panel">
        <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
          <PackagePlus color="var(--accent-primary)"/> Crear Pedido (GraphQL)
        </h2>
        
        {message && <div className="badge badge-success" style={{ marginBottom: '1rem', display: 'inline-block' }}>{message}</div>}

        <form onSubmit={handleCreateOrder} className="grid-layout" style={{ gap: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Email Cliente</label>
            <input type="email" required className="input-field" value={email} onChange={e => setEmail(e.target.value)} />
          </div>
          <div className="grid-layout grid-cols-2" style={{ gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Origen</label>
              <input type="text" required className="input-field" value={origin} onChange={e => setOrigin(e.target.value)} />
            </div>
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Destino</label>
              <input type="text" required className="input-field" value={destination} onChange={e => setDestination(e.target.value)} />
            </div>
          </div>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Peso (Kg)</label>
            <input type="number" required className="input-field" value={weight} onChange={e => setWeight(e.target.value)} />
          </div>
          
          <button type="submit" className="btn btn-primary" disabled={creating} style={{ marginTop: '1rem' }}>
            {creating ? <div className="loader"></div> : 'Generar Pedido'}
          </button>
        </form>
      </div>

      {/* Columna 2: Pedidos Activos y Asignación REST */}
      <div className="glass-panel">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2 style={{ margin: 0 }}>Pedidos Activos</h2>
          <button onClick={fetchPedidos} className="btn btn-secondary" style={{ padding: '0.5rem' }}>
            <RefreshCw size={18} className={loading ? 'spinning' : ''}/>
          </button>
        </div>

        {loading ? (
          <p>Cargando vía REST API...</p>
        ) : pedidos.length > 0 ? (
          <div className="grid-layout" style={{ gap: '1rem' }}>
            {pedidos.map((pedido: any) => (
              <div key={pedido.id} style={{ background: 'rgba(0,0,0,0.2)', padding: '1rem', borderRadius: '8px', border: '1px solid var(--glass-border)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                  <strong>{pedido.id}</strong>
                  <span className="badge badge-warning">{pedido.status}</span>
                </div>
                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>{pedido.origin} &rarr; {pedido.destination}</p>
                
                {selectedOrderForAssign === pedido.id ? (
                  <div style={{ marginTop: '1rem', padding: '1rem', background: 'rgba(0,0,0,0.3)', borderRadius: '8px' }}>
                    <h4 style={{ margin: '0 0 1rem 0' }}>Seleccionar Vehículo Disponible</h4>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                      {vehicles.length > 0 ? vehicles.map(v => (
                        <div key={v.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '0.5rem', background: 'rgba(255,255,255,0.05)', borderRadius: '4px' }}>
                          <div>
                            <strong>{v.plate}</strong> <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{v.type}</span>
                          </div>
                          <button 
                            onClick={() => handleAssignVehicle(pedido.id, v.id)}
                            disabled={assigning}
                            className="btn btn-primary"
                            style={{ padding: '0.25rem 0.75rem', fontSize: '0.875rem' }}
                          >
                            Asignar
                          </button>
                        </div>
                      )) : <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>No hay vehículos disponibles.</p>}
                    </div>
                    <button 
                      onClick={() => setSelectedOrderForAssign(null)} 
                      className="btn btn-secondary" 
                      style={{ width: '100%', marginTop: '1rem', padding: '0.5rem' }}
                    >
                      Cancelar
                    </button>
                  </div>
                ) : (
                  <button 
                    onClick={() => setSelectedOrderForAssign(pedido.id)}
                    className="btn btn-primary" 
                    style={{ width: '100%', padding: '0.5rem' }}
                  >
                    <Truck size={16}/> Asignar Vehículo (Manual)
                  </button>
                )}
              </div>
            ))}
          </div>
        ) : (
          <p style={{ color: 'var(--text-secondary)' }}>No hay pedidos activos.</p>
        )}
      </div>
    </div>
  );
}
