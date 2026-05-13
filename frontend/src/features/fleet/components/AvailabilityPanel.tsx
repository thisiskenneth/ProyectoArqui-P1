import type { FleetAvailability } from "../../../types/fleet";
import { statusColors, statusLabels, typeIcons } from "../constants";
import { Gauge } from "lucide-react";

interface Props {
  availability: FleetAvailability;
}

export function AvailabilityPanel({ availability }: Props) {
  return (
    <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl p-6">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 rounded-xl bg-emerald-500/20 flex items-center justify-center">
          <Gauge className="w-5 h-5 text-emerald-400" />
        </div>
        <div>
          <h2 className="text-lg font-semibold text-white">Disponibilidad para ruteo</h2>
          <p className="text-sm text-slate-400">Recursos listos para asignación</p>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <h3 className="text-xs font-medium text-slate-400 uppercase tracking-wider mb-3">Vehículos ({availability.vehicles.length})</h3>
          {availability.vehicles.length > 0 ? (
            <div className="space-y-2">
              {availability.vehicles.map((v) => (
                <div key={v.id} className="flex items-center justify-between bg-white/[0.04] rounded-lg px-4 py-2.5 border border-white/[0.06]">
                  <span className="text-sm text-white">{typeIcons[v.type]} {v.plate}</span>
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium border ${statusColors[v.status]}`}>{statusLabels[v.status]}</span>
                </div>
              ))}
            </div>
          ) : <p className="text-sm text-slate-500 italic">Sin vehículos disponibles</p>}
        </div>
        <div>
          <h3 className="text-xs font-medium text-slate-400 uppercase tracking-wider mb-3">Conductores ({availability.drivers.length})</h3>
          {availability.drivers.length > 0 ? (
            <div className="space-y-2">
              {availability.drivers.map((d) => (
                <div key={d.id} className="flex items-center gap-3 bg-white/[0.04] rounded-lg px-4 py-2.5 border border-white/[0.06]">
                  <div className="w-7 h-7 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400 text-xs font-bold">{d.firstName[0]}{d.lastName[0]}</div>
                  <span className="text-sm text-white">{d.firstName} {d.lastName}</span>
                </div>
              ))}
            </div>
          ) : <p className="text-sm text-slate-500 italic">Sin conductores disponibles</p>}
        </div>
      </div>
    </div>
  );
}
