import type { Vehicle } from "../../../types/fleet";
import { statusColors, statusLabels, typeIcons, vehicleStatuses, vehicleTypes } from "../constants";
import type { VehicleStatus, VehicleType } from "../../../types/fleet";
import { Pencil, Trash2, Plus, X, Truck } from "lucide-react";
import type { FormEvent } from "react";

interface Props {
  vehicles: Vehicle[];
  form: Vehicle;
  onFormChange: (v: Vehicle) => void;
  onSubmit: (e: FormEvent<HTMLFormElement>) => void;
  onReset: () => void;
  onEdit: (v: Vehicle) => void;
  onDelete: (id?: string) => void;
}

export function VehicleSection({ vehicles, form, onFormChange, onSubmit, onReset, onEdit, onDelete }: Props) {
  const isEditing = !!form.id;

  return (
    <div className="space-y-6">
      {/* Form */}
      <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-10 h-10 rounded-xl bg-blue-500/20 flex items-center justify-center">
            <Truck className="w-5 h-5 text-blue-400" />
          </div>
          <div>
            <h2 className="text-lg font-semibold text-white">
              {isEditing ? "Editar vehículo" : "Registrar vehículo"}
            </h2>
            <p className="text-sm text-slate-400">
              {isEditing ? "Modifica los datos del vehículo" : "Añade un nuevo vehículo a la flota"}
            </p>
          </div>
        </div>

        <form onSubmit={onSubmit} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Matrícula</label>
            <input
              type="text"
              required
              minLength={2}
              maxLength={20}
              pattern="^[A-Za-z0-9\-]+$"
              title="Solo letras, números y guiones"
              placeholder="ABC-1234"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-blue-500/50 focus:ring-1 focus:ring-blue-500/30 transition-all"
              value={form.plate}
              onChange={(e) => onFormChange({ ...form, plate: e.target.value.toUpperCase() })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Tipo</label>
            <select
              required
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white outline-none focus:border-blue-500/50 focus:ring-1 focus:ring-blue-500/30 transition-all"
              value={form.type}
              onChange={(e) => onFormChange({ ...form, type: e.target.value as VehicleType })}
            >
              {vehicleTypes.map((t) => (
                <option key={t} value={t} className="bg-slate-800">
                  {typeIcons[t]} {t}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Capacidad (kg)</label>
            <input
              type="number"
              required
              min={1}
              max={50000}
              step={0.1}
              placeholder="500"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-blue-500/50 focus:ring-1 focus:ring-blue-500/30 transition-all"
              value={form.capacityKg || ""}
              onChange={(e) => onFormChange({ ...form, capacityKg: parseFloat(e.target.value) || 0 })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Autonomía (km)</label>
            <input
              type="number"
              min={0}
              max={5000}
              step={0.1}
              placeholder="300"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-blue-500/50 focus:ring-1 focus:ring-blue-500/30 transition-all"
              value={form.autonomyKm || ""}
              onChange={(e) => onFormChange({ ...form, autonomyKm: parseFloat(e.target.value) || 0 })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Estado</label>
            <select
              required
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white outline-none focus:border-blue-500/50 focus:ring-1 focus:ring-blue-500/30 transition-all"
              value={form.status}
              onChange={(e) => onFormChange({ ...form, status: e.target.value as VehicleStatus })}
            >
              {vehicleStatuses.map((s) => (
                <option key={s} value={s} className="bg-slate-800">
                  {statusLabels[s]}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-end gap-2">
            <button
              type="submit"
              className="flex-1 flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-500 text-white font-medium text-sm px-4 py-2.5 rounded-lg transition-all duration-200 active:scale-95 cursor-pointer"
            >
              <Plus className="w-4 h-4" />
              {isEditing ? "Actualizar" : "Registrar"}
            </button>
            {isEditing && (
              <button
                type="button"
                onClick={onReset}
                className="flex items-center justify-center bg-white/[0.06] hover:bg-white/[0.12] text-slate-300 px-3 py-2.5 rounded-lg transition-all duration-200 cursor-pointer"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>
        </form>
      </div>

      {/* Table */}
      {vehicles.length > 0 && (
        <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-white/[0.08]">
                  <th className="text-left text-xs font-medium text-slate-400 px-6 py-3.5">Vehículo</th>
                  <th className="text-left text-xs font-medium text-slate-400 px-6 py-3.5">Tipo</th>
                  <th className="text-right text-xs font-medium text-slate-400 px-6 py-3.5">Capacidad</th>
                  <th className="text-right text-xs font-medium text-slate-400 px-6 py-3.5">Autonomía</th>
                  <th className="text-center text-xs font-medium text-slate-400 px-6 py-3.5">Estado</th>
                  <th className="text-center text-xs font-medium text-slate-400 px-6 py-3.5">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/[0.05]">
                {vehicles.map((v) => (
                  <tr
                    key={v.id}
                    className="group hover:bg-white/[0.03] transition-colors"
                  >
                    <td className="px-6 py-3.5">
                      <span className="font-mono font-semibold text-white tracking-wide">{v.plate}</span>
                    </td>
                    <td className="px-6 py-3.5">
                      <span className="text-slate-300">{typeIcons[v.type]} {v.type}</span>
                    </td>
                    <td className="px-6 py-3.5 text-right text-slate-300 font-mono">
                      {v.capacityKg?.toLocaleString()} kg
                    </td>
                    <td className="px-6 py-3.5 text-right text-slate-300 font-mono">
                      {v.autonomyKm?.toLocaleString()} km
                    </td>
                    <td className="px-6 py-3.5 text-center">
                      <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border ${statusColors[v.status]}`}>
                        {statusLabels[v.status]}
                      </span>
                    </td>
                    <td className="px-6 py-3.5">
                      <div className="flex items-center justify-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button
                          onClick={() => onEdit(v)}
                          className="p-1.5 rounded-lg hover:bg-blue-500/20 text-slate-400 hover:text-blue-400 transition-colors cursor-pointer"
                          title="Editar"
                        >
                          <Pencil className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => onDelete(v.id)}
                          className="p-1.5 rounded-lg hover:bg-rose-500/20 text-slate-400 hover:text-rose-400 transition-colors cursor-pointer"
                          title="Eliminar"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {vehicles.length === 0 && (
        <div className="bg-white/[0.04] border border-white/[0.08] rounded-2xl p-12 text-center">
          <Truck className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No hay vehículos registrados</p>
          <p className="text-sm text-slate-500 mt-1">Usa el formulario para agregar el primero</p>
        </div>
      )}
    </div>
  );
}
