import { useState, useEffect } from 'react';
import { gql, useMutation, useQuery } from '@apollo/client';
import { api } from '../lib/api';
import { PackagePlus, Truck, RefreshCw } from 'lucide-react';

const CREAR_PEDIDO = gql`
  mutation CrearPedido($input: PedidoInput!) {
    crearPedido(input: $input) {
      id
      customerEmail
      status
    }
  }
`;

// Simulación de query ya que el schema de graphql del backend puede requerir el clienteId
const PEDIDOS_ACTIVOS = gql`
  query {
    pedidosActivos(clienteId: "admin") {
      id
      customerEmail
      origin
      destination
      status
    }
  }
`;

export default function OperatorPanel() {
  const [email, setEmail] = useState('');
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [weight, setWeight] = useState('');
  
  // GraphQL
  const [crearPedido, { loading: creating }] = useMutation(CREAR_PEDIDO);
  const { data, loading, refetch } = useQuery(PEDIDOS_ACTIVOS, { errorPolicy: 'ignore' });

  // REST state
  const [assigning, setAssigning] = useState(false);
  const [message, setMessage] = useState('');

  const handleCreateOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await crearPedido({
        variables: {
          input: {
            customerEmail: email,
            origin,
            destination,
            weightKg: parseFloat(weight),
            geographicLevel: 'LOCAL',
            vehicleType: 'LIGHT'
          }
        }
      });
      setMessage('Pedido creado exitosamente (GraphQL)');
      refetch();
      setEmail(''); setOrigin(''); setDestination(''); setWeight('');
    } catch (err) {
      setMessage('Error al crear pedido (Revisa si GraphQL Gateway está vivo)');
    }
  };

  const handleAssignVehicle = async (orderId: string) => {
    setAssigning(true);
    try {
      await api.post('/shipments/assign', {
        orderId,
        origin: 'Bodega Central',
        destination: 'Cliente Final',
        customerEmail: 'admin@logiflow.com'
      });
      setMessage(`Vehículo asignado al pedido ${orderId} (REST)`);
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
          <button onClick={() => refetch()} className="btn btn-secondary" style={{ padding: '0.5rem' }}>
            <RefreshCw size={18} className={loading ? 'spinning' : ''}/>
          </button>
        </div>

        {loading ? (
          <p>Cargando vía GraphQL...</p>
        ) : data?.pedidosActivos?.length > 0 ? (
          <div className="grid-layout" style={{ gap: '1rem' }}>
            {data.pedidosActivos.map((pedido: any) => (
              <div key={pedido.id} style={{ background: 'rgba(0,0,0,0.2)', padding: '1rem', borderRadius: '8px', border: '1px solid var(--glass-border)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                  <strong>{pedido.id}</strong>
                  <span className="badge badge-warning">{pedido.status}</span>
                </div>
                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>{pedido.origin} &rarr; {pedido.destination}</p>
                
                <button 
                  onClick={() => handleAssignVehicle(pedido.id)}
                  disabled={assigning}
                  className="btn btn-primary" 
                  style={{ width: '100%', padding: '0.5rem' }}
                >
                  <Truck size={16}/> Asignar Vehículo (REST)
                </button>
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
