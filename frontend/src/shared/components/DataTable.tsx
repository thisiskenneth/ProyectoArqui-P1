import type { ReactNode } from "react";

interface DataTableProps {
  headers: string[];
  rows: ReactNode[][];
}

export function DataTable({ headers, rows }: DataTableProps) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>{headers.map((header) => <th key={header}>{header}</th>)}</tr>
        </thead>
        <tbody>
          {rows.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((cell, cellIndex) => <td key={cellIndex}>{cell}</td>)}
            </tr>
          ))}
          {rows.length === 0 && (
            <tr>
              <td className="empty-row" colSpan={headers.length}>Sin registros</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
