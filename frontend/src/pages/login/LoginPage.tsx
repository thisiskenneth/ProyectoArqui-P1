import React from 'react';

const LoginPage: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md">
        <h1 className="text-2xl font-bold text-center mb-6">Iniciar Sesión - LogiFlow</h1>
        <form className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Usuario / Correo</label>
            <input type="text" className="mt-1 w-full p-2 border rounded-md" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Contraseña</label>
            <input type="password" title="password" className="mt-1 w-full p-2 border rounded-md" />
          </div>
          <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-md hover:bg-indigo-700">
            Ingresar
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
