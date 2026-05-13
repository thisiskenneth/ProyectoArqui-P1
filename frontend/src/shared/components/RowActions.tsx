interface RowActionsProps {
  onEdit: () => void;
  onDelete: () => void;
}

export function RowActions({ onEdit, onDelete }: RowActionsProps) {
  return (
    <div className="row-actions">
      <button type="button" onClick={onEdit}>Editar</button>
      <button type="button" className="danger-button" onClick={onDelete}>Eliminar</button>
    </div>
  );
}
