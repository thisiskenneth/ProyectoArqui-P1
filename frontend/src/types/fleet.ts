export type VehicleStatus = "AVAILABLE" | "MAINTENANCE" | "BUSY" | "INACTIVE";
export type VehicleType = "Moto" | "Auto" | "Furgoneta" | "Camion";

export interface Vehicle {
  id?: string;
  plate: string;
  type: VehicleType;
  capacityKg: number;
  autonomyKm: number;
  status: VehicleStatus;
}

export interface Driver {
  id?: string;
  firstName: string;
  lastName: string;
  licenseNumber: string;
  phone: string;
  available: boolean;
}

export interface FleetAvailability {
  vehicles: Vehicle[];
  drivers: Driver[];
}
