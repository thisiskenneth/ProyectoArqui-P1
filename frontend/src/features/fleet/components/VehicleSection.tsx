import type { FormEvent } from "react";
import { DataTable } from "../../../shared/components/DataTable";
import { RowActions } from "../../../shared/components/RowActions";
import type { Vehicle, VehicleStatus } from "../../../types/fleet";
import { vehicleStatuses } from "../constants";

interface VehicleSectionProps {
  vehicles: Vehicle[];
  form: Vehicle;
  onFormChange: (vehicle: Vehicle) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  onReset: () => void;
  onDelete: (id?: number) => void;
}

export function VehicleSection({ vehicles, form, onFormChange, onSubmit, onReset, onDelete }: VehicleSectionProps) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <h2>Vehiculos</h2>
        {form.id && <button type="button" onClick={onReset}>Nuevo</button>}
      </div>
      <form className="form-grid" onSubmit={onSubmit}>
        <label>
          Matricula
          <input value={form.plate} onChange={(event) => onFormChange({ ...form, plate: event.target.value })} required />
        </label>
        <label>
          Tipo
          <select value={form.type} onChange={(event) => onFormChange({ ...form, type: event.target.value })}>
            <option>Moto</option>
            <option>Auto</option>
            <option>Furgoneta</option>
            <option>Camion</option>
          </select>
        </label>
        <label>
          Capacidad kg
          <input type="number" min="1" value={form.capacityKg} onChange={(event) => onFormChange({ ...form, capacityKg: Number(event.target.value) })} required />
        </label>
        <label>
          Autonomia km
          <input type="number" min="1" value={form.autonomyKm} onChange={(event) => onFormChange({ ...form, autonomyKm: Number(event.target.value) })} required />
        </label>
        <label>
          Estado
          <select value={form.status} onChange={(event) => onFormChange({ ...form, status: event.target.value as VehicleStatus })}>
            {vehicleStatuses.map((status) => <option key={status}>{status}</option>)}
          </select>
        </label>
        <button className="primary-button" type="submit">{form.id ? "Actualizar vehiculo" : "Crear vehiculo"}</button>
      </form>
      <DataTable
        headers={["Matricula", "Tipo", "Capacidad", "Estado", "Acciones"]}
        rows={vehicles.map((vehicle) => [
          vehicle.plate,
          vehicle.type,
          `${vehicle.capacityKg} kg`,
          vehicle.status,
          <RowActions key={vehicle.id} onEdit={() => onFormChange(vehicle)} onDelete={() => onDelete(vehicle.id)} />
        ])}
      />
    </section>
  );
}
