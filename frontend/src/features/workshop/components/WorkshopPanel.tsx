import type { FormEvent } from "react";
import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../../../types/workshop";
import { Wrench, Search, ClipboardPlus } from "lucide-react";

interface Props {
  plate: string;
  description: string;
  vehicleResult: WorkshopVehicleResult | null;
  orderResult: MaintenanceOrderResult | null;
  onPlateChange: (v: string) => void;
  onDescriptionChange: (v: string) => void;
  onQueryVehicle: (e: FormEvent<HTMLFormElement>) => void;
  onRegisterOrder: (e: FormEvent<HTMLFormElement>) => void;
}

export function WorkshopPanel({ plate, description, vehicleResult, orderResult, onPlateChange, onDescriptionChange, onQueryVehicle, onRegisterOrder }: Props) {
  return (
    <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl p-6">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 rounded-xl bg-amber-500/20 flex items-center justify-center">
          <Wrench className="w-5 h-5 text-amber-400" />
        </div>
        <div>
          <h2 className="text-lg font-semibold text-white">Taller SOAP</h2>
          <p className="text-sm text-slate-400">Consulta y mantenimiento vía ms-taller-soap</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Consultar */}
        <div className="space-y-4">
          <form onSubmit={onQueryVehicle} className="space-y-3">
            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1.5">Matrícula del vehículo</label>
              <input type="text" required placeholder="ABC-1234" className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-amber-500/50 transition-all" value={plate} onChange={(e) => onPlateChange(e.target.value.toUpperCase())} />
            </div>
            <button type="submit" className="w-full flex items-center justify-center gap-2 bg-amber-600 hover:bg-amber-500 text-white font-medium text-sm px-4 py-2.5 rounded-lg transition-all active:scale-95 cursor-pointer">
              <Search className="w-4 h-4" /> Consultar vehículo
            </button>
          </form>
          {vehicleResult && (
            <div className="bg-white/[0.04] border border-amber-500/20 rounded-xl p-4 space-y-2">
              <p className="text-xs text-slate-400">Resultado SOAP</p>
              <div className="grid grid-cols-2 gap-2 text-sm">
                <span className="text-slate-400">Matrícula:</span><span className="text-white font-mono">{vehicleResult.matricula}</span>
                <span className="text-slate-400">Estado:</span><span className="text-emerald-400 font-medium">{vehicleResult.estado}</span>
                <span className="text-slate-400">Último mant.:</span><span className="text-white">{vehicleResult.ultimoMantenimiento}</span>
                <span className="text-slate-400 col-span-2">Observaciones:</span>
                <span className="text-white col-span-2 text-xs">{vehicleResult.observaciones}</span>
              </div>
            </div>
          )}
        </div>

        {/* Registrar */}
        <div className="space-y-4">
          <form onSubmit={onRegisterOrder} className="space-y-3">
            <div>
              <label className="block text-xs font-medium text-slate-400 mb-1.5">Descripción de mantenimiento</label>
              <textarea required rows={3} placeholder="Revisión preventiva..." className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-amber-500/50 transition-all resize-none" value={description} onChange={(e) => onDescriptionChange(e.target.value)} />
            </div>
            <button type="submit" className="w-full flex items-center justify-center gap-2 bg-amber-600 hover:bg-amber-500 text-white font-medium text-sm px-4 py-2.5 rounded-lg transition-all active:scale-95 cursor-pointer">
              <ClipboardPlus className="w-4 h-4" /> Registrar orden
            </button>
          </form>
          {orderResult && (
            <div className="bg-white/[0.04] border border-emerald-500/20 rounded-xl p-4 space-y-2">
              <p className="text-xs text-emerald-400 font-medium">✓ Orden registrada</p>
              <div className="grid grid-cols-2 gap-2 text-sm">
                <span className="text-slate-400">Código:</span><span className="text-white font-mono">{orderResult.codigoOrden}</span>
                <span className="text-slate-400">Fecha:</span><span className="text-white">{orderResult.fechaIngreso}</span>
                <span className="text-slate-400 col-span-2">Mensaje:</span>
                <span className="text-white col-span-2 text-xs">{orderResult.mensaje}</span>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
