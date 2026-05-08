import React from 'react';

const FleetDashboard: React.FC = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Administración de Flota</h1>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="font-semibold mb-2">Estado de Vehículos</h2>
          <ul className="divide-y divide-gray-200">
            <li className="py-2 flex justify-between"><span>Camión #101</span> <span className="text-green-600 font-medium">Activo</span></li>
            <li className="py-2 flex justify-between"><span>Camión #102</span> <span className="text-red-600 font-medium">En Taller</span></li>
            <li className="py-2 flex justify-between"><span>Van #205</span> <span className="text-green-600 font-medium">Activo</span></li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default FleetDashboard;
