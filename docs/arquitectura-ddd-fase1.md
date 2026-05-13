# LogiFlow - Documento DDD y propuesta de arquitectura Fase 1

## 1. Alcance

Este documento cubre el descubrimiento estrategico del dominio LogiFlow solicitado para Fase 1. Describe el dominio completo para poder defender la arquitectura futura, pero la implementacion de esta rama se limita a dos pilotos:

- `ms-flota-rest`: API REST para flota.
- `ms-taller-soap`: API REST para taller.

Los demas contextos se documentan como parte del analisis DDD, no como servicios ejecutables de Fase 1.

## 2. Dominio y subdominios

### Core Domain

Gestion de la cadena de entrega: asignacion inteligente de pedidos a vehiculos considerando nivel geografico, carga, disponibilidad, ubicacion, optimizacion de rutas y seguimiento.

### Subdominios de soporte

- Gestion de Flota.
- Gestion de Clientes.
- Gestion de Tarifas y Facturacion.
- Notificaciones.

### Subdominios genericos

- Autenticacion y autorizacion.
- Integracion con taller mecanico mediante REST.

## 3. Bounded Contexts

| Contexto | Tipo | Responsabilidad | Estado en Fase 1 |
| --- | --- | --- | --- |
| Pedidos | Core relacionado | Recepcion, ciclo de estados y cierre de pedidos | Modelado, no implementado |
| Ruteo y Asignacion | Core Domain | Asignar pedidos a vehiculos y calcular rutas | Modelado, no implementado |
| Seguimiento | Core relacionado | Posicion y trazabilidad en tiempo real | Modelado, no implementado |
| Flota | Soporte | Vehiculos, conductores, disponibilidad y caracteristicas tecnicas | Implementado como `ms-flota-rest` |
| Taller | Generico / ACL | Contrato REST para taller mecanico externo | Implementado como `ms-taller-soap` |
| Clientes | Soporte | Datos maestros de clientes y cuentas | Modelado, no implementado |
| Facturacion | Soporte | Tarifas, costos y facturas | Modelado, no implementado |
| Notificaciones | Soporte | Avisos por canal ante eventos relevantes | Modelado, no implementado |
| Autenticacion | Generico | Usuarios, roles y tokens | Modelado, no implementado |
| GraphQL Gateway | BFF | Agregacion de datos para frontend | Modelado, no implementado |

## 4. Lenguaje ubicuo

### Pedidos

- Pedido: solicitud de transporte de uno o varios paquetes.
- Origen: punto inicial de recoleccion.
- Destino: punto final de entrega.
- Paquete: unidad fisica transportada.
- Estado: Creado, Asignado, En ruta, Entregado, Cancelado.
- Prioridad: nivel de urgencia operativa.

### Flota

- Vehiculo: recurso movil disponible para transportar paquetes.
- Matricula: identificador unico del vehiculo.
- Tipo: Moto, Auto, Furgoneta o Camion.
- Capacidad: carga maxima en kilogramos.
- Autonomia: distancia aproximada disponible antes de recarga o reabastecimiento.
- Estado: Disponible, En servicio, Mantenimiento o Inactivo.
- Conductor: persona habilitada para operar un vehiculo.
- Disponibilidad: condicion consultable por ruteo para asignar recursos.

### Taller

- OrdenMantenimiento: registro de una incidencia o trabajo mecanico.
- Descripcion: detalle de la necesidad de mantenimiento.
- FechaIngreso: momento en que se registra la orden.
- EstadoTaller: resultado devuelto por el sistema de taller.

### Ruteo y Asignacion

- Envio: asociacion entre Pedido y Vehiculo.
- Ruta: secuencia planificada de tramos.
- Parada: punto intermedio de recoleccion o entrega.
- ETA: tiempo estimado de llegada.
- Kilometros: distancia calculada.

### Seguimiento

- Posicion: coordenadas latitud/longitud.
- Velocidad: rapidez reportada por el vehiculo.
- Tramo: segmento actual de la ruta.
- EventoSeguimiento: cambio relevante de posicion o estado.

### Clientes

- Cliente: persona o empresa que solicita entregas.
- CuentaCorporativa: cuenta empresarial con condiciones propias.
- Contrato: acuerdo comercial asociado a una cuenta.
- Saldo: valor pendiente o disponible.

### Facturacion

- Tarifa: regla de calculo de costo.
- Trayecto: nivel local, provincial o nacional.
- Recargo: valor adicional por peso, urgencia o distancia.
- Factura: documento de cobro.

### Notificaciones

- Evento: hecho que dispara una comunicacion.
- Destinatario: cliente, operador o conductor.
- Canal: email, SMS o push.

### Autenticacion

- Usuario: identidad del sistema.
- Rol: Cliente, Conductor, Operador o Admin.
- Token: credencial temporal de acceso.

### GraphQL Gateway

- Query: consulta agregada.
- Mutation: operacion de escritura.
- Resolver: componente que resuelve datos desde proveedores.
- Schema: contrato GraphQL.

## 5. Event Storming

### Flujo de pedido y asignacion

| Comando | Evento resultante | Agregado | Contexto |
| --- | --- | --- | --- |
| RegistrarPedido | PedidoCreado | Pedido | Pedidos |
| ValidarPedido | PedidoValidado | Pedido | Pedidos |
| CancelarPedido | PedidoCancelado | Pedido | Pedidos |
| SolicitarAsignacion | AsignacionSolicitada | Envio | Ruteo y Asignacion |
| ConsultarDisponibilidadFlota | DisponibilidadConsultada | Vehiculo / Conductor | Flota |
| AsignarVehiculo | VehiculoAsignado | Envio | Ruteo y Asignacion |
| ConfirmarEnvio | EnvioAsignado | Envio | Ruteo y Asignacion |

### Flujo de seguimiento

| Comando | Evento resultante | Agregado | Contexto |
| --- | --- | --- | --- |
| IniciarRuta | EnvioEnRuta | Envio | Seguimiento |
| ReportarPosicion | PosicionActualizada | TrackingLog | Seguimiento |
| ReportarIncidencia | IncidenciaReportada | TrackingLog | Seguimiento |
| ConfirmarEntrega | PedidoEntregado | Pedido | Pedidos |

### Flujo de flota y taller

| Comando | Evento resultante | Agregado | Contexto |
| --- | --- | --- | --- |
| CrearVehiculo | VehiculoRegistrado | Vehiculo | Flota |
| ActualizarVehiculo | VehiculoActualizado | Vehiculo | Flota |
| CambiarEstadoVehiculo | EstadoVehiculoCambiado | Vehiculo | Flota |
| CrearConductor | ConductorRegistrado | Conductor | Flota |
| ConsultarVehiculoTaller | VehiculoConsultadoEnTaller | OrdenMantenimiento | Taller |
| RegistrarOrdenMantenimiento | OrdenMantenimientoRegistrada | OrdenMantenimiento | Taller |

### Flujo comercial y comunicaciones

| Comando | Evento resultante | Agregado | Contexto |
| --- | --- | --- | --- |
| RegistrarCliente | ClienteRegistrado | Cliente | Clientes |
| CalcularTarifa | TarifaCalculada | Factura | Facturacion |
| EmitirFactura | FacturaEmitida | Factura | Facturacion |
| EnviarNotificacion | NotificacionEnviada | Notificacion | Notificaciones |

## 6. Agregados principales

- Pedido: protege consistencia del ciclo de vida de un pedido.
- Envio: protege la relacion entre pedido, vehiculo, conductor y ruta.
- Vehiculo: protege matricula, tipo, capacidad, autonomia y estado.
- Conductor: protege licencia, datos personales y disponibilidad.
- OrdenMantenimiento: protege registro de mantenimiento y respuesta del taller.
- Cliente: protege datos maestros y cuenta.
- Factura: protege calculo y emision de cobro.
- TrackingLog: protege el historial de posiciones.
- Usuario: protege credenciales y roles.

## 7. Context Map

| Relacion | Patron | Descripcion |
| --- | --- | --- |
| Flota - Ruteo | Partnership | Ruteo depende de disponibilidad precisa y Flota ajusta su API para facilitar asignaciones. |
| Flota - consumidores internos | Open Host Service | Flota publica REST estable para consultar vehiculos, conductores y disponibilidad. |
| Pedidos - Ruteo | Customer/Supplier | Ruteo consume pedidos creados y devuelve asignaciones. Pedidos es proveedor del ciclo de pedido. |
| Ruteo - Seguimiento | Customer/Supplier | Seguimiento consume envios activos para reportar posiciones y ETA. |
| Pedidos - Facturacion | Conformist | Facturacion adopta el modelo de eventos de Pedidos para facturar entregas. |
| Taller - Flota | Anticorruption Layer | Taller traduce el contrato REST externo al lenguaje interno de mantenimiento de flota. |
| Notificaciones - Pedidos/Ruteo/Seguimiento | Published Language | Notificaciones interpreta eventos publicados con nombres de dominio acordados. |
| GraphQL Gateway - proveedores | Customer/Supplier | Gateway actua como cliente de servicios REST y de lectura para armar vistas de frontend. |
| Autenticacion - servicios | Shared Kernel minimo | Los servicios comparten conceptos de usuario, rol y token sin compartir logica de negocio. |

## 8. Decisiones de arquitectura para Fase 1

- `ms-flota-rest` expone solo REST y documenta su contrato con Swagger/OpenAPI.
- `ms-taller-soap` expone solo REST y documenta su contrato con Swagger/OpenAPI.
- No se ejecutan buses de eventos, gateway GraphQL, WebSockets ni Kubernetes en esta rama.
- El directorio `frontend/`, si permanece en el repositorio, queda fuera del entregable de Fase 1 y no participa en el pipeline ni en `docker-compose.yml`.
- El pipeline valida unicamente los dos servicios de Fase 1.
- Las ramas de referencia son `main` para produccion y `development` para integracion.

## 9. Contratos piloto

### REST Flota

- CRUD vehiculos: `/api/vehicles`.
- CRUD conductores: `/api/drivers`.
- Disponibilidad para ruteo:
  - `/api/vehicles/available`
  - `/api/drivers/available`
  - `/api/fleet/availability`

### REST Taller

- Consulta de vehiculo: `/api/vehiculos/{matricula}`.
- Registro de orden de mantenimiento: `/api/mantenimientos`.

## 10. Riesgos y supuestos

- La propuesta estrategica incluye contextos futuros porque Fase 1 lo exige, pero su implementacion queda fuera de esta rama.
- El servicio de taller simula persistencia de ordenes; Fase 1 solo exige exponer contrato y operacion piloto.
- SonarCloud y Telegram dependen de secretos configurados en GitHub.
