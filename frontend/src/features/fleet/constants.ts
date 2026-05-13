import type { Driver, Vehicle, VehicleStatus, VehicleType } from "../../types/fleet";

export const vehicleStatuses: VehicleStatus[] = ["AVAILABLE", "BUSY", "MAINTENANCE", "INACTIVE"];
export const vehicleTypes: VehicleType[] = ["Moto", "Auto", "Furgoneta", "Camion"];

export const statusLabels: Record<VehicleStatus, string> = {
  AVAILABLE: "Disponible",
  BUSY: "En servicio",
  MAINTENANCE: "Mantenimiento",
  INACTIVE: "Inactivo"
};

export const statusColors: Record<VehicleStatus, string> = {
  AVAILABLE: "bg-emerald-500/20 text-emerald-400 border-emerald-500/30",
  BUSY: "bg-blue-500/20 text-blue-400 border-blue-500/30",
  MAINTENANCE: "bg-amber-500/20 text-amber-400 border-amber-500/30",
  INACTIVE: "bg-gray-500/20 text-gray-400 border-gray-500/30"
};

export const typeIcons: Record<VehicleType, string> = {
  Moto: "🏍️",
  Auto: "🚗",
  Furgoneta: "🚐",
  Camion: "🚛"
};

export const emptyVehicle: Vehicle = {
  plate: "",
  type: "Moto",
  capacityKg: 20,
  autonomyKm: 120,
  status: "AVAILABLE"
};

export const emptyDriver: Driver = {
  firstName: "",
  lastName: "",
  licenseNumber: "",
  phone: "",
  available: true
};
