# LogiFlow - Plataforma Logística

Estructura profesional para sistema de logística basado en microservicios y frontend modular.

## Estructura del Proyecto

- `/frontend`: React + Vite + TypeScript.
- `/backend`: Arquitectura de microservicios.
  - `ms-auth`: Autenticación y Autorización.
  - `ms-clientes`: Gestión de clientes.
  - `ms-pedidos`: Gestión de órdenes de envío.
  - `ms-ruteo`: Optimización de rutas.
  - `ms-seguimiento`: Tracking en tiempo real.
  - `ms-flota-rest`: Gestión de vehículos (REST).
  - `ms-taller-soap`: Integración con talleres (SOAP).
  - `ms-facturacion`: Generación de facturas.
  - `ms-notificaciones`: Email/SMS/Push.
  - `graphql-gateway`: Puerta de enlace unificada.
- `/infrastructure`: Configuraciones de K8s y Docker.

## Desarrollo

### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Backend
Cada servicio es independiente. Se puede usar Docker Compose para levantarlos todos.
```bash
docker-compose up --build
```
