export interface WorkshopVehicleResult {
  matricula: string;
  estado: string;
  ultimoMantenimiento: string;
  observaciones: string;
}

export interface MaintenanceOrderResult {
  codigoOrden: string;
  fechaIngreso: string;
  mensaje: string;
}
