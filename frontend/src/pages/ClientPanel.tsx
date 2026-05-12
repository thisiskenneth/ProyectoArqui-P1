import { useState } from 'react';
import { gql, useQuery } from '@apollo/client';
import { api } from '../lib/api';
import { FileText, Package } from 'lucide-react';

const PEDIDOS_CLIENTE = gql`
  query PedidosCliente($clienteId: String!) {
    pedidosActivos(clienteId: $clienteId) {
      id
      origin
      destination
      status
    }
  }
`;

export default function ClientPanel() {
  const [email, setEmail] = useState('admin@logiflow.com');
  const [searchedEmail, setSearchedEmail] = useState('');
  
  const { data, loading, refetch } = useQuery(PEDIDOS_CLIENTE, {
    variables: { clienteId: searchedEmail },
    skip: !searchedEmail,
    errorPolicy: 'ignore'
  });

  const [invoices, setInvoices] = useState<any[]>([]);
  const [loadingInvoices, setLoadingInvoices] = useState(false);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setSearchedEmail(email);
    fetchInvoices(email);
  };

  const fetchInvoices = async (clientEmail: string) => {
    setLoadingInvoices(true);
    try {
      // Endpoint depende de ms-facturacion
      const res = await api.get('/invoices'); // Fetch all for simplicity, or filter by client if API supports
      // Simulamos filtro en frontend si el API no soporta query params por email
      const filtered = res.data.filter((inv: any) => inv.clientId === clientEmail);
      setInvoices(filtered.length > 0 ? filtered : res.data); // Fallback to all if property mismatched
    } catch (err) {
      console.error('Error fetching invoices', err);
    } finally {
      setLoadingInvoices(false);
    }
  };

  return (
    <div className="grid-layout" style={{ gap: '2rem' }}>
      <div className="glass-panel" style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
        <div style={{ flex: 1 }}>
          <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Correo del Cliente</label>
          <input 
            type="email" 
            className="input-field" 
            value={email} 
            onChange={e => setEmail(e.target.value)} 
          />
        </div>
        <button onClick={handleSearch} className="btn btn-primary">Buscar Datos</button>
      </div>

      {searchedEmail && (
        <div className="grid-layout grid-cols-2">
          {/* Historial de Pedidos */}
          <div className="glass-panel">
            <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
              <Package color="var(--accent-primary)"/> Mis Pedidos (GraphQL)
            </h2>
            
            {loading ? <p>Cargando pedidos...</p> : data?.pedidosActivos?.length > 0 ? (
              <div className="grid-layout" style={{ gap: '1rem' }}>
                {data.pedidosActivos.map((pedido: any) => (
                  <div key={pedido.id} style={{ padding: '1rem', background: 'rgba(0,0,0,0.2)', borderRadius: '8px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                      <strong>{pedido.id}</strong>
                      <span className="badge badge-success">{pedido.status}</span>
                    </div>
                    <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                      Ruta: {pedido.origin} - {pedido.destination}
                    </p>
                  </div>
                ))}
              </div>
            ) : <p>No hay pedidos activos para este cliente.</p>}
          </div>

          {/* Facturas */}
          <div className="glass-panel">
            <h2 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
              <FileText color="var(--success)"/> Mis Facturas (REST)
            </h2>

            {loadingInvoices ? <p>Cargando facturas...</p> : invoices.length > 0 ? (
              <div className="grid-layout" style={{ gap: '1rem' }}>
                {invoices.map((inv: any) => (
                  <div key={inv.id || inv.invoiceNumber} style={{ padding: '1rem', border: '1px solid var(--glass-border)', borderRadius: '8px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                      <strong>{inv.invoiceNumber}</strong>
                      <span>${inv.totalAmount?.toFixed(2)}</span>
                    </div>
                    <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                      Pedido: {inv.orderId} | Estado: {inv.status}
                    </p>
                    <button className="btn btn-secondary" style={{ width: '100%', marginTop: '1rem', padding: '0.5rem' }}>
                      Descargar PDF
                    </button>
                  </div>
                ))}
              </div>
            ) : <p>No hay facturas emitidas.</p>}
          </div>
        </div>
      )}
    </div>
  );
}
