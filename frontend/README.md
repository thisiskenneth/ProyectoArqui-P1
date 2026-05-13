# LogiFlow Frontend Fase 1

Frontend creado con `npm create vite@latest frontend -- --template react-ts`.

## Alcance

- CRUD de vehiculos contra `ms-flota-rest`.
- CRUD de conductores contra `ms-flota-rest`.
- Consulta de disponibilidad contra `ms-flota-rest`.
- Consulta SOAP `consultarVehiculo`.
- Registro SOAP `registrarOrdenMantenimiento`.

No incluye funcionalidades de fases posteriores.

## Estructura

```text
src/
  app/
  features/
    fleet/
    workshop/
  services/
  shared/
  styles/
  types/
```

## Ejecucion

```bash
npm install
npm run dev
```

El servidor de Vite queda en `http://localhost:5173` y usa proxy local:

- `/api` hacia `http://localhost:8081`
- `/soap` hacia `http://localhost:8089/ws`
