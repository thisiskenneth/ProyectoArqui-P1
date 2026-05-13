// frontend/src/app/App.tsx
import type { FormEvent } from "react";
import { useEffect, useMemo, useState } from "react";
import { AvailabilityPanel } from "../features/fleet/components/AvailabilityPanel";
import { DriverSection } from "../features/fleet/components/DriverSection";
import { VehicleSection } from "../features/fleet/components/VehicleSection";
import { emptyDriver, emptyVehicle } from "../features/fleet/constants";
import { WorkshopPanel } from "../features/workshop/components/WorkshopPanel";
import { MetricCard } from "../shared/components/MetricCard";
import { StatusMessage } from "../shared/components/StatusMessage";
import {
  deleteDriver,
  deleteVehicle,
  getFleetAvailability,
  listDrivers,
  listVehicles,
  saveDriver,
  saveVehicle
} from "../services/fleetApi";
import { consultarVehiculo, registrarOrdenMantenimiento } from "../services/workshopSoapApi";
import type { Driver, FleetAvailability, Vehicle } from "../types/fleet";
import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../types/workshop";

export function App() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [drivers, setDrivers] = useState<Driver[]>([]);
  const [availability, setAvailability] = useState<FleetAvailability>({ vehicles: [], drivers: [] });
  const [vehicleForm, setVehicleForm] = useState<Vehicle>(emptyVehicle);
  const [driverForm, setDriverForm] = useState<Driver>(emptyDriver);
  const [soapPlate, setSoapPlate] = useState("ABC-1234");
  const [soapDescription, setSoapDescription] = useState("Revision preventiva");
  const [soapVehicle, setSoapVehicle] = useState<WorkshopVehicleResult | null>(null);
  const [soapOrder, setSoapOrder] = useState<MaintenanceOrderResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("Listo para operar Fase 1.");

  const totals = useMemo(() => ({
    vehicles: vehicles.length,
    drivers: drivers.length,
    availableVehicles: availability.vehicles.length,
    availableDrivers: availability.drivers.length
  }), [availability.drivers.length, availability.vehicles.length, drivers.length, vehicles.length]);

  async function refreshData() {
    setLoading(true);
    try {
      const [vehicleData, driverData, availabilityData] = await Promise.all([
        listVehicles(),
        listDrivers(),
        getFleetAvailability()
      ]);
      setVehicles(vehicleData);
      setDrivers(driverData);
      setAvailability(availabilityData);
      setMessage("Datos sincronizados con ms-flota-rest.");
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo sincronizar.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    queueMicrotask(() => {
      void refreshData();
    });
  }, []);

  async function submitVehicle(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      await saveVehicle(vehicleForm);
      setVehicleForm(emptyVehicle);
      await refreshData();
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo guardar el vehiculo.");
    }
  }

  async function submitDriver(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      await saveDriver(driverForm);
      setDriverForm(emptyDriver);
      await refreshData();
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo guardar el conductor.");
    }
  }

  async function removeVehicle(id?: number) {
    if (!id) return;
    await deleteVehicle(id);
    await refreshData();
  }

  async function removeDriver(id?: number) {
    if (!id) return;
    await deleteDriver(id);
    await refreshData();
  }

  async function queryWorkshopVehicle(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      setSoapVehicle(await consultarVehiculo(soapPlate));
      setMessage("Consulta SOAP completada contra ms-taller-soap.");
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo consultar el vehiculo.");
    }
  }

  async function registerMaintenanceOrder(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      setSoapOrder(await registrarOrdenMantenimiento(soapPlate, soapDescription));
      setMessage("Orden SOAP registrada contra ms-taller-soap.");
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo registrar la orden.");
    }
  }

  return (
    <main className="app-shell">
      <header className="topbar">
        <div className="brand-block">
          <img src="/logiflow-mark.svg" alt="" width="48" height="48" />
          <div>
            <p className="eyebrow">LogiFlow Fase 1</p>
            <h1>Panel de flota y taller</h1>
          </div>
        </div>
        <button className="primary-button" onClick={() => void refreshData()} disabled={loading}>
          {loading ? "Sincronizando" : "Sincronizar"}
        </button>
      </header>

      <section className="metrics" aria-label="Resumen operativo">
        <MetricCard label="Vehiculos" value={totals.vehicles} />
        <MetricCard label="Conductores" value={totals.drivers} />
        <MetricCard label="Vehiculos disponibles" value={totals.availableVehicles} />
        <MetricCard label="Conductores disponibles" value={totals.availableDrivers} />
      </section>

      <StatusMessage message={message} />

      <section className="workspace">
        <VehicleSection
          vehicles={vehicles}
          form={vehicleForm}
          onFormChange={setVehicleForm}
          onSubmit={(event) => void submitVehicle(event)}
          onReset={() => setVehicleForm(emptyVehicle)}
          onDelete={(id) => void removeVehicle(id)}
        />
        <DriverSection
          drivers={drivers}
          form={driverForm}
          onFormChange={setDriverForm}
          onSubmit={(event) => void submitDriver(event)}
          onReset={() => setDriverForm(emptyDriver)}
          onDelete={(id) => void removeDriver(id)}
        />
        <AvailabilityPanel availability={availability} />
        <WorkshopPanel
          plate={soapPlate}
          description={soapDescription}
          vehicleResult={soapVehicle}
          orderResult={soapOrder}
          onPlateChange={setSoapPlate}
          onDescriptionChange={setSoapDescription}
          onQueryVehicle={(event) => void queryWorkshopVehicle(event)}
          onRegisterOrder={(event) => void registerMaintenanceOrder(event)}
        />
      </section>
    </main>
  );
}
