import type { Driver } from "../../../types/fleet";
import { Pencil, Trash2, Plus, X, UserCheck } from "lucide-react";
import type { FormEvent } from "react";

interface Props {
  drivers: Driver[];
  form: Driver;
  onFormChange: (d: Driver) => void;
  onSubmit: (e: FormEvent<HTMLFormElement>) => void;
  onReset: () => void;
  onEdit: (d: Driver) => void;
  onDelete: (id?: string) => void;
}

export function DriverSection({ drivers, form, onFormChange, onSubmit, onReset, onEdit, onDelete }: Props) {
  const isEditing = !!form.id;

  return (
    <div className="space-y-6">
      {/* Form */}
      <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-10 h-10 rounded-xl bg-violet-500/20 flex items-center justify-center">
            <UserCheck className="w-5 h-5 text-violet-400" />
          </div>
          <div>
            <h2 className="text-lg font-semibold text-white">
              {isEditing ? "Editar conductor" : "Registrar conductor"}
            </h2>
            <p className="text-sm text-slate-400">
              {isEditing ? "Modifica los datos del conductor" : "Añade un nuevo conductor al equipo"}
            </p>
          </div>
        </div>

        <form onSubmit={onSubmit} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Nombre</label>
            <input
              type="text"
              required
              minLength={2}
              maxLength={50}
              placeholder="Juan"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-violet-500/50 focus:ring-1 focus:ring-violet-500/30 transition-all"
              value={form.firstName}
              onChange={(e) => onFormChange({ ...form, firstName: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Apellido</label>
            <input
              type="text"
              required
              minLength={2}
              maxLength={50}
              placeholder="Pérez"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-violet-500/50 focus:ring-1 focus:ring-violet-500/30 transition-all"
              value={form.lastName}
              onChange={(e) => onFormChange({ ...form, lastName: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Licencia</label>
            <input
              type="text"
              required
              minLength={5}
              maxLength={30}
              placeholder="LIC-00123"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-violet-500/50 focus:ring-1 focus:ring-violet-500/30 transition-all"
              value={form.licenseNumber}
              onChange={(e) => onFormChange({ ...form, licenseNumber: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Teléfono</label>
            <input
              type="tel"
              placeholder="+593 99 123 4567"
              className="w-full bg-white/[0.06] border border-white/[0.1] rounded-lg px-3 py-2.5 text-sm text-white placeholder-slate-500 outline-none focus:border-violet-500/50 focus:ring-1 focus:ring-violet-500/30 transition-all"
              value={form.phone}
              onChange={(e) => onFormChange({ ...form, phone: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1.5">Disponibilidad</label>
            <button
              type="button"
              onClick={() => onFormChange({ ...form, available: !form.available })}
              className={`w-full flex items-center justify-between px-3 py-2.5 rounded-lg border text-sm font-medium transition-all cursor-pointer ${
                form.available
                  ? "bg-emerald-500/15 border-emerald-500/30 text-emerald-400"
                  : "bg-white/[0.06] border-white/[0.1] text-slate-400"
              }`}
            >
              <span>{form.available ? "Disponible" : "No disponible"}</span>
              <div className={`w-10 h-5 rounded-full relative transition-colors ${form.available ? "bg-emerald-500" : "bg-slate-600"}`}>
                <div className={`absolute top-0.5 w-4 h-4 rounded-full bg-white shadow transition-transform ${form.available ? "translate-x-5" : "translate-x-0.5"}`} />
              </div>
            </button>
          </div>

          <div className="flex items-end gap-2">
            <button
              type="submit"
              className="flex-1 flex items-center justify-center gap-2 bg-violet-600 hover:bg-violet-500 text-white font-medium text-sm px-4 py-2.5 rounded-lg transition-all duration-200 active:scale-95 cursor-pointer"
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
      {drivers.length > 0 && (
        <div className="bg-white/[0.04] backdrop-blur-xl border border-white/[0.08] rounded-2xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-white/[0.08]">
                  <th className="text-left text-xs font-medium text-slate-400 px-6 py-3.5">Conductor</th>
                  <th className="text-left text-xs font-medium text-slate-400 px-6 py-3.5">Licencia</th>
                  <th className="text-left text-xs font-medium text-slate-400 px-6 py-3.5">Teléfono</th>
                  <th className="text-center text-xs font-medium text-slate-400 px-6 py-3.5">Estado</th>
                  <th className="text-center text-xs font-medium text-slate-400 px-6 py-3.5">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/[0.05]">
                {drivers.map((d) => (
                  <tr key={d.id} className="group hover:bg-white/[0.03] transition-colors">
                    <td className="px-6 py-3.5">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-violet-500/20 flex items-center justify-center text-violet-400 text-xs font-bold">
                          {d.firstName[0]}{d.lastName[0]}
                        </div>
                        <span className="text-white font-medium">{d.firstName} {d.lastName}</span>
                      </div>
                    </td>
                    <td className="px-6 py-3.5 text-slate-300 font-mono text-xs">{d.licenseNumber}</td>
                    <td className="px-6 py-3.5 text-slate-300">{d.phone || "—"}</td>
                    <td className="px-6 py-3.5 text-center">
                      <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border ${
                        d.available
                          ? "bg-emerald-500/20 text-emerald-400 border-emerald-500/30"
                          : "bg-gray-500/20 text-gray-400 border-gray-500/30"
                      }`}>
                        {d.available ? "Disponible" : "No disponible"}
                      </span>
                    </td>
                    <td className="px-6 py-3.5">
                      <div className="flex items-center justify-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button
                          onClick={() => onEdit(d)}
                          className="p-1.5 rounded-lg hover:bg-violet-500/20 text-slate-400 hover:text-violet-400 transition-colors cursor-pointer"
                          title="Editar"
                        >
                          <Pencil className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => onDelete(d.id)}
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

      {drivers.length === 0 && (
        <div className="bg-white/[0.04] border border-white/[0.08] rounded-2xl p-12 text-center">
          <UserCheck className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No hay conductores registrados</p>
          <p className="text-sm text-slate-500 mt-1">Usa el formulario para agregar el primero</p>
        </div>
      )}
    </div>
  );
}
