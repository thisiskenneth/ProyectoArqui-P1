import { useState } from 'react';
import { api } from '../lib/api';
import { useNavigate } from 'react-router-dom';
import { Lock, UserPlus } from 'lucide-react';

export default function Login() {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      if (isLogin) {
        const res = await api.post('/auth/login', { username, password });
        localStorage.setItem('token', res.data.token);
        navigate('/');
        window.location.reload(); 
      } else {
        await api.post('/auth/register', { username, password, email, roles: ['OPERATOR'] });
        setSuccess('Cuenta creada exitosamente. Por favor, inicia sesión.');
        setIsLogin(true);
      }
    } catch (err: any) {
      console.error(err);
      setError(isLogin ? 'Credenciales inválidas o servicio no disponible' : 'Error al registrar la cuenta');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
      <div className="glass-panel" style={{ width: '100%', maxWidth: '400px' }}>
        <div style={{ display: 'flex', marginBottom: '2rem', borderBottom: '1px solid var(--glass-border)' }}>
          <button 
            className={`btn ${isLogin ? 'btn-primary' : ''}`} 
            style={{ flex: 1, borderRadius: '8px 0 0 0', background: isLogin ? '' : 'transparent' }}
            onClick={() => { setIsLogin(true); setError(''); setSuccess(''); }}
          >
            <Lock size={16} style={{ marginRight: '0.5rem', display: 'inline' }}/> Iniciar Sesión
          </button>
          <button 
            className={`btn ${!isLogin ? 'btn-primary' : ''}`} 
            style={{ flex: 1, borderRadius: '0 8px 0 0', background: !isLogin ? '' : 'transparent' }}
            onClick={() => { setIsLogin(false); setError(''); setSuccess(''); }}
          >
            <UserPlus size={16} style={{ marginRight: '0.5rem', display: 'inline' }}/> Registrarse
          </button>
        </div>

        {error && <div className="badge badge-danger" style={{ marginBottom: '1rem', display: 'block', textAlign: 'center' }}>{error}</div>}
        {success && <div className="badge badge-success" style={{ marginBottom: '1rem', display: 'block', textAlign: 'center' }}>{success}</div>}
        
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Usuario</label>
            <input 
              type="text" 
              className="input-field" 
              value={username}
              onChange={e => setUsername(e.target.value)}
              required
            />
          </div>
          {!isLogin && (
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Correo Electrónico</label>
              <input 
                type="email" 
                className="input-field" 
                value={email}
                onChange={e => setEmail(e.target.value)}
                required={!isLogin}
              />
            </div>
          )}
          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>Contraseña</label>
            <input 
              type="password" 
              className="input-field" 
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              minLength={6}
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading} style={{ marginTop: '1rem' }}>
            {loading ? 'Procesando...' : (isLogin ? 'Acceder' : 'Crear Cuenta')}
          </button>
        </form>
      </div>
    </div>
  );
}
