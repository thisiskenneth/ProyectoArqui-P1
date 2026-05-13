export type VehicleStatus = "AVAILABLE" | "MAINTENANCE" | "BUSY" | "INACTIVE";

export interface Vehicle {
  id?: number;
  plate: string;
  type: string;
  capacityKg: number;
  autonomyKm: number;
  status: VehicleStatus;
}

export interface Driver {
  id?: number;
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
