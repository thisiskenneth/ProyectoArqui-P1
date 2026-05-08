import React from 'react';

const OperatorDashboard: React.FC = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Panel del Operador Logístico</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white p-4 rounded shadow border-l-4 border-blue-500">
          <p className="text-gray-500">Pedidos Pendientes</p>
          <p className="text-3xl font-bold">24</p>
        </div>
        <div className="bg-white p-4 rounded shadow border-l-4 border-yellow-500">
          <p className="text-gray-500">En Tránsito</p>
          <p className="text-3xl font-bold">12</p>
        </div>
        <div className="bg-white p-4 rounded shadow border-l-4 border-green-500">
          <p className="text-gray-500">Entregados Hoy</p>
          <p className="text-3xl font-bold">45</p>
        </div>
      </div>
    </div>
  );
};

export default OperatorDashboard;
