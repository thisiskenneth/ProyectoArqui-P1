import React from 'react';

const ClientDashboard: React.FC = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Mi Panel de Cliente</h1>
      <p className="mb-6">Bienvenido, aquí puede gestionar sus envíos y facturación.</p>
      <div className="bg-white overflow-hidden shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6 text-center text-gray-500">
          No tiene pedidos activos en este momento.
        </div>
      </div>
    </div>
  );
};

export default ClientDashboard;
