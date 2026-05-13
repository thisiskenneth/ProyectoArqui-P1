import type { FleetAvailability } from "../../../types/fleet";

interface AvailabilityPanelProps {
  availability: FleetAvailability;
}

export function AvailabilityPanel({ availability }: AvailabilityPanelProps) {
  return (
    <section className="panel">
      <h2>Disponibilidad para ruteo</h2>
      <div className="availability-grid">
        <div>
          <h3>Vehiculos disponibles</h3>
          <ul>
            {availability.vehicles.map((vehicle) => <li key={vehicle.id}>{vehicle.plate} - {vehicle.type}</li>)}
            {availability.vehicles.length === 0 && <li>Sin vehiculos disponibles</li>}
          </ul>
        </div>
        <div>
          <h3>Conductores disponibles</h3>
          <ul>
            {availability.drivers.map((driver) => <li key={driver.id}>{driver.firstName} {driver.lastName}</li>)}
            {availability.drivers.length === 0 && <li>Sin conductores disponibles</li>}
          </ul>
        </div>
      </div>
    </section>
  );
}
