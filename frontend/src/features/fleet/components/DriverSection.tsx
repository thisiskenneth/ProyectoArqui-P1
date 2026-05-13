import type { FormEvent } from "react";
import { DataTable } from "../../../shared/components/DataTable";
import { RowActions } from "../../../shared/components/RowActions";
import type { Driver } from "../../../types/fleet";

interface DriverSectionProps {
  drivers: Driver[];
  form: Driver;
  onFormChange: (driver: Driver) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  onReset: () => void;
  onDelete: (id?: number) => void;
}

export function DriverSection({ drivers, form, onFormChange, onSubmit, onReset, onDelete }: DriverSectionProps) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <h2>Conductores</h2>
        {form.id && <button type="button" onClick={onReset}>Nuevo</button>}
      </div>
      <form className="form-grid" onSubmit={onSubmit}>
        <label>
          Nombres
          <input value={form.firstName} onChange={(event) => onFormChange({ ...form, firstName: event.target.value })} required />
        </label>
        <label>
          Apellidos
          <input value={form.lastName} onChange={(event) => onFormChange({ ...form, lastName: event.target.value })} required />
        </label>
        <label>
          Licencia
          <input value={form.licenseNumber} onChange={(event) => onFormChange({ ...form, licenseNumber: event.target.value })} required />
        </label>
        <label>
          Telefono
          <input value={form.phone} onChange={(event) => onFormChange({ ...form, phone: event.target.value })} />
        </label>
        <label className="switch-row">
          <input type="checkbox" checked={form.available} onChange={(event) => onFormChange({ ...form, available: event.target.checked })} />
          Disponible
        </label>
        <button className="primary-button" type="submit">{form.id ? "Actualizar conductor" : "Crear conductor"}</button>
      </form>
      <DataTable
        headers={["Nombre", "Licencia", "Telefono", "Disponible", "Acciones"]}
        rows={drivers.map((driver) => [
          `${driver.firstName} ${driver.lastName}`,
          driver.licenseNumber,
          driver.phone || "-",
          driver.available ? "Si" : "No",
          <RowActions key={driver.id} onEdit={() => onFormChange(driver)} onDelete={() => onDelete(driver.id)} />
        ])}
      />
    </section>
  );
}
