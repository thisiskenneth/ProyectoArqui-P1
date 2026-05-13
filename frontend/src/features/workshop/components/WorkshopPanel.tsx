import type { FormEvent } from "react";
import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../../../types/workshop";

interface WorkshopPanelProps {
  plate: string;
  description: string;
  vehicleResult: WorkshopVehicleResult | null;
  orderResult: MaintenanceOrderResult | null;
  onPlateChange: (value: string) => void;
  onDescriptionChange: (value: string) => void;
  onQueryVehicle: (event: FormEvent<HTMLFormElement>) => void;
  onRegisterOrder: (event: FormEvent<HTMLFormElement>) => void;
}

export function WorkshopPanel({
  plate,
  description,
  vehicleResult,
  orderResult,
  onPlateChange,
  onDescriptionChange,
  onQueryVehicle,
  onRegisterOrder
}: WorkshopPanelProps) {
  return (
    <section className="panel">
      <h2>Taller SOAP</h2>
      <form className="form-grid soap-form" onSubmit={onQueryVehicle}>
        <label>
          Matricula
          <input value={plate} onChange={(event) => onPlateChange(event.target.value)} required />
        </label>
        <button className="primary-button" type="submit">Consultar vehiculo</button>
      </form>
      {vehicleResult && (
        <dl className="result-list">
          <dt>Estado</dt><dd>{vehicleResult.estado}</dd>
          <dt>Ultimo mantenimiento</dt><dd>{vehicleResult.ultimoMantenimiento}</dd>
          <dt>Observaciones</dt><dd>{vehicleResult.observaciones}</dd>
        </dl>
      )}
      <form className="form-grid soap-form" onSubmit={onRegisterOrder}>
        <label>
          Descripcion
          <input value={description} onChange={(event) => onDescriptionChange(event.target.value)} required />
        </label>
        <button className="primary-button" type="submit">Registrar orden</button>
      </form>
      {orderResult && (
        <dl className="result-list">
          <dt>Orden</dt><dd>{orderResult.codigoOrden}</dd>
          <dt>Ingreso</dt><dd>{orderResult.fechaIngreso}</dd>
          <dt>Mensaje</dt><dd>{orderResult.mensaje}</dd>
        </dl>
      )}
    </section>
  );
}
