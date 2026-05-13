// src/app/App.tsx
import type { FormEvent } from "react";
import { useEffect, useMemo, useState } from "react";
import { AvailabilityPanel } from "../features/fleet/components/AvailabilityPanel";
import { DriverSection } from "../features/fleet/components/DriverSection";
import { VehicleSection } from "../features/fleet/components/VehicleSection";
import { emptyDriver, emptyVehicle } from "../features/fleet/constants";
import { WorkshopPanel } from "../features/workshop/components/WorkshopPanel";
import { deleteDriver, deleteVehicle, getFleetAvailability, listDrivers, listVehicles, saveDriver, saveVehicle } from "../services/fleetApi";
import { consultarVehiculo, registrarOrdenMantenimiento } from "../services/workshopSoapApi";
import type { Driver, FleetAvailability, Vehicle } from "../types/fleet";
import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../types/workshop";
import { Truck, UserCheck, Gauge, Wrench, RefreshCw, AlertCircle, CheckCircle } from "lucide-react";

type Tab = "vehicles" | "drivers" | "availability" | "workshop";

export function App() {
  const [tab, setTab] = useState<Tab>("vehicles");
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
  const [toast, setToast] = useState<{ message: string; type: "ok" | "err" } | null>(null);

  const totals = useMemo(() => ({
    vehicles: vehicles.length,
    drivers: drivers.length,
    availableVehicles: availability.vehicles.length,
    availableDrivers: availability.drivers.length
  }), [availability.drivers.length, availability.vehicles.length, drivers.length, vehicles.length]);

  function showToast(message: string, type: "ok" | "err" = "ok") {
    setToast({ message, type });
    setTimeout(() => setToast(null), 4000);
  }

  async function refreshData() {
    setLoading(true);
    try {
      const [v, d, a] = await Promise.all([listVehicles(), listDrivers(), getFleetAvailability()]);
      setVehicles(v);
      setDrivers(d);
      setAvailability(a);
    } catch (e) {
      showToast(e instanceof Error ? e.message : "Error de conexión", "err");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { void refreshData(); }, []);

  async function submitVehicle(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    try {
      await saveVehicle(vehicleForm);
      setVehicleForm(emptyVehicle);
      showToast(vehicleForm.id ? "Vehículo actualizado" : "Vehículo registrado");
      await refreshData();
    } catch (err) { showToast(err instanceof Error ? err.message : "Error al guardar", "err"); }
  }

  async function submitDriver(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    try {
      await saveDriver(driverForm);
      setDriverForm(emptyDriver);
      showToast(driverForm.id ? "Conductor actualizado" : "Conductor registrado");
      await refreshData();
    } catch (err) { showToast(err instanceof Error ? err.message : "Error al guardar", "err"); }
  }

  async function removeVehicle(id?: string) {
    if (!id) return;
    try { await deleteVehicle(id); showToast("Vehículo eliminado"); await refreshData(); }
    catch (err) { showToast(err instanceof Error ? err.message : "Error", "err"); }
  }

  async function removeDriver(id?: string) {
    if (!id) return;
    try { await deleteDriver(id); showToast("Conductor eliminado"); await refreshData(); }
    catch (err) { showToast(err instanceof Error ? err.message : "Error", "err"); }
  }

  async function queryWorkshopVehicle(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    try { setSoapVehicle(await consultarVehiculo(soapPlate)); showToast("Consulta SOAP completada"); }
    catch (err) { showToast(err instanceof Error ? err.message : "Error SOAP", "err"); }
  }

  async function registerMaintenanceOrder(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    try { setSoapOrder(await registrarOrdenMantenimiento(soapPlate, soapDescription)); showToast("Orden SOAP registrada"); }
    catch (err) { showToast(err instanceof Error ? err.message : "Error SOAP", "err"); }
  }

  const tabs: { id: Tab; label: string; icon: typeof Truck; count?: number; color: string }[] = [
    { id: "vehicles", label: "Vehículos", icon: Truck, count: totals.vehicles, color: "blue" },
    { id: "drivers", label: "Conductores", icon: UserCheck, count: totals.drivers, color: "violet" },
    { id: "availability", label: "Disponibilidad", icon: Gauge, count: totals.availableVehicles + totals.availableDrivers, color: "emerald" },
    { id: "workshop", label: "Taller SOAP", icon: Wrench, color: "amber" }
  ];

  return (
    <div className="min-h-screen">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-navy-900/80 backdrop-blur-xl border-b border-white/[0.06]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-500 to-cyan-500 flex items-center justify-center text-white font-bold text-lg shadow-lg shadow-blue-500/20">L</div>
            <div>
              <h1 className="text-lg font-bold text-white tracking-tight">LogiFlow</h1>
              <p className="text-xs text-slate-500">Fase 1 · Panel de flota y taller</p>
            </div>
          </div>
          <button onClick={() => void refreshData()} disabled={loading} className="flex items-center gap-2 bg-white/[0.06] hover:bg-white/[0.12] border border-white/[0.1] text-slate-300 text-sm font-medium px-4 py-2 rounded-lg transition-all disabled:opacity-50 cursor-pointer">
            <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin" : ""}`} />
            {loading ? "Sincronizando…" : "Sincronizar"}
          </button>
        </div>

        {/* Metrics bar */}
        <div className="max-w-7xl mx-auto px-4 sm:px-6 pb-3 grid grid-cols-2 sm:grid-cols-4 gap-3">
          {[
            { label: "Vehículos", value: totals.vehicles, color: "text-blue-400" },
            { label: "Conductores", value: totals.drivers, color: "text-violet-400" },
            { label: "Vehículos disp.", value: totals.availableVehicles, color: "text-emerald-400" },
            { label: "Conductores disp.", value: totals.availableDrivers, color: "text-cyan-400" }
          ].map((m) => (
            <div key={m.label} className="bg-white/[0.04] rounded-xl px-4 py-2.5 border border-white/[0.06]">
              <p className="text-xs text-slate-500">{m.label}</p>
              <p className={`text-2xl font-bold ${m.color}`}>{m.value}</p>
            </div>
          ))}
        </div>

        {/* Tabs */}
        <div className="max-w-7xl mx-auto px-4 sm:px-6">
          <nav className="flex gap-1 overflow-x-auto pb-px -mb-px">
            {tabs.map((t) => {
              const active = tab === t.id;
              return (
                <button key={t.id} onClick={() => setTab(t.id)} className={`flex items-center gap-2 px-4 py-2.5 text-sm font-medium rounded-t-lg border-b-2 transition-all whitespace-nowrap cursor-pointer ${active ? `border-${t.color}-500 text-white bg-white/[0.04]` : "border-transparent text-slate-400 hover:text-slate-300 hover:bg-white/[0.03]"}`}>
                  <t.icon className="w-4 h-4" />
                  {t.label}
                  {t.count !== undefined && (
                    <span className={`text-xs px-1.5 py-0.5 rounded-full ${active ? "bg-white/10" : "bg-white/[0.06]"}`}>{t.count}</span>
                  )}
                </button>
              );
            })}
          </nav>
        </div>
      </header>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6">
        {tab === "vehicles" && (
          <VehicleSection vehicles={vehicles} form={vehicleForm} onFormChange={setVehicleForm} onSubmit={(e) => void submitVehicle(e)} onReset={() => setVehicleForm(emptyVehicle)} onEdit={(v) => setVehicleForm(v)} onDelete={(id) => void removeVehicle(id)} />
        )}
        {tab === "drivers" && (
          <DriverSection drivers={drivers} form={driverForm} onFormChange={setDriverForm} onSubmit={(e) => void submitDriver(e)} onReset={() => setDriverForm(emptyDriver)} onEdit={(d) => setDriverForm(d)} onDelete={(id) => void removeDriver(id)} />
        )}
        {tab === "availability" && <AvailabilityPanel availability={availability} />}
        {tab === "workshop" && (
          <WorkshopPanel plate={soapPlate} description={soapDescription} vehicleResult={soapVehicle} orderResult={soapOrder} onPlateChange={setSoapPlate} onDescriptionChange={setSoapDescription} onQueryVehicle={(e) => void queryWorkshopVehicle(e)} onRegisterOrder={(e) => void registerMaintenanceOrder(e)} />
        )}
      </main>

      {/* Toast */}
      {toast && (
        <div className={`fixed bottom-6 right-6 z-50 flex items-center gap-2 px-4 py-3 rounded-xl shadow-2xl text-sm font-medium border animate-in slide-in-from-bottom-4 ${toast.type === "ok" ? "bg-emerald-950/90 border-emerald-500/30 text-emerald-300" : "bg-rose-950/90 border-rose-500/30 text-rose-300"}`}>
          {toast.type === "ok" ? <CheckCircle className="w-4 h-4" /> : <AlertCircle className="w-4 h-4" />}
          {toast.message}
        </div>
      )}
    </div>
  );
}
