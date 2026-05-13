import type { Driver, Vehicle, VehicleStatus } from "../../types/fleet";

export const vehicleStatuses: VehicleStatus[] = ["AVAILABLE", "BUSY", "MAINTENANCE", "INACTIVE"];

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
