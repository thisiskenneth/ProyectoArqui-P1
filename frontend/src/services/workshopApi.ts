import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../types/workshop";
import { jsonHeaders, readJson } from "./http";

interface MaintenanceInfoApi {
  plate: string;
  status: string;
  lastMaintenance: string;
  notes: string;
}

export async function consultarVehiculo(vehicleId: string): Promise<WorkshopVehicleResult> {
  const info = await readJson<MaintenanceInfoApi>(
    await fetch(`/api/vehicles/${vehicleId}/maintenance`)
  );

  return {
    matricula: info.plate,
    estado: info.status,
    ultimoMantenimiento: info.lastMaintenance,
    observaciones: info.notes
  };
}

export async function registrarOrdenMantenimiento(
  vehicleId: string,
  descripcion: string
): Promise<MaintenanceOrderResult> {
  return readJson<MaintenanceOrderResult>(
    await fetch(`/api/vehicles/${vehicleId}/maintenance-orders`, {
      method: "POST",
      headers: jsonHeaders,
      body: JSON.stringify({ descripcion })
    })
  );
}
