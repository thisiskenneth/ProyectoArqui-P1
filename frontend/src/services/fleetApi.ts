import type { Driver, FleetAvailability, Vehicle } from "../types/fleet";
import { jsonHeaders, readJson } from "./http";

export async function listVehicles(): Promise<Vehicle[]> {
  return readJson<Vehicle[]>(await fetch("/api/vehicles"));
}

export async function saveVehicle(vehicle: Vehicle): Promise<Vehicle> {
  const response = await fetch(vehicle.id ? `/api/vehicles/${vehicle.id}` : "/api/vehicles", {
    method: vehicle.id ? "PUT" : "POST",
    headers: jsonHeaders,
    body: JSON.stringify(vehicle)
  });

  return readJson<Vehicle>(response);
}

export async function deleteVehicle(id: string): Promise<void> {
  const response = await fetch(`/api/vehicles/${id}`, { method: "DELETE" });
  if (!response.ok) {
    throw new Error(await response.text());
  }
}

export async function listDrivers(): Promise<Driver[]> {
  return readJson<Driver[]>(await fetch("/api/drivers"));
}

export async function saveDriver(driver: Driver): Promise<Driver> {
  const response = await fetch(driver.id ? `/api/drivers/${driver.id}` : "/api/drivers", {
    method: driver.id ? "PUT" : "POST",
    headers: jsonHeaders,
    body: JSON.stringify(driver)
  });

  return readJson<Driver>(response);
}

export async function deleteDriver(id: string): Promise<void> {
  const response = await fetch(`/api/drivers/${id}`, { method: "DELETE" });
  if (!response.ok) {
    throw new Error(await response.text());
  }
}

export async function getFleetAvailability(): Promise<FleetAvailability> {
  return readJson<FleetAvailability>(await fetch("/api/fleet/availability"));
}
