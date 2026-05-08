import React from 'react';

const TrackingPage: React.FC = () => {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Seguimiento Público de Pedidos</h1>
      <p className="text-gray-600">Ingrese su número de guía para rastrear su paquete.</p>
      {/* Search Input Placeholder */}
      <div className="mt-6 max-w-md">
        <input 
          type="text" 
          placeholder="Ej: LOGI-123456" 
          className="w-full p-3 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 outline-none"
        />
        <button className="mt-4 w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition">
          Buscar Guía
        </button>
      </div>
    </div>
  );
};

export default TrackingPage;
