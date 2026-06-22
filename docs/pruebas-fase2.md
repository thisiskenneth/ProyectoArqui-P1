# Guía de pruebas — LogiFlow Fase 2 (Postman · RabbitMQ · GraphQL)

Catálogo completo de pruebas para el backend de Fase 2 ejecutándose **localmente**
(microservicios en IntelliJ/`mvnw` + PostgreSQL local + RabbitMQ en Docker).

> La colección lista para importar está en la raíz: **`LogiFlow.postman_collection.json`**
> (Postman → *Import*). Las variables de URL e IDs se capturan solas entre peticiones.

---

## 0. Requisitos previos

1. PostgreSQL local en `localhost:5432` (`postgres` / `1234`) con las bases creadas
   (`infrastructure/local/create-databases.sql`).
2. RabbitMQ en Docker: `docker compose up -d rabbitmq` → Management en http://localhost:15672 (`guest`/`guest`).
3. Los 8 microservicios arriba.

| Servicio | Puerto | API | Swagger UI |
| --- | ---: | --- | --- |
| ms-auth | 8088 | REST | http://localhost:8088/swagger-ui.html |
| ms-clientes | 8086 | REST | http://localhost:8086/swagger-ui.html |
| ms-flota-rest | 8081 | REST | http://localhost:8081/swagger-ui.html |
| ms-taller | 8089 | REST | http://localhost:8089/swagger-ui.html |
| ms-pedidos | 8087 | REST + eventos | http://localhost:8087/swagger-ui.html |
| ms-ruteo | 8082 | REST + eventos | http://localhost:8082/swagger-ui.html |
| ms-seguimiento | 8083 | WebSocket (STOMP) | — |
| graphql-gateway | 8080 | GraphQL | http://localhost:8080/graphiql |
| RabbitMQ | 5672 / 15672 | AMQP / HTTP | http://localhost:15672 |

Convención de respuestas: `200` OK, `201` creado, `204` sin contenido, `400` entrada inválida,
`404` no encontrado, `401` no autorizado, `409` conflicto/duplicado.

---

## 1. Pruebas REST

### 1.1 ms-auth (8088)

| # | Método | URL | Body | Espera |
| --- | --- | --- | --- | ---: |
| 1 | POST | `/api/auth/register` | `{"username":"operador","password":"LogiFlow-2026","email":"operador@logiflow.ec","roles":["OPERATOR"]}` | 200 |
| 2 | POST | `/api/auth/login` | `{"username":"operador","password":"LogiFlow-2026"}` | 200 + `token` |
| 3 | POST | `/api/auth/verify?token={token}` | — | 200 → `true` |

Roles válidos: `ADMIN`, `OPERATOR`, `CLIENT`, `DRIVER`.

**Negativos (validación):**

| Caso | Body / URL | Espera |
| --- | --- | ---: |
| Password < 6 | `{"username":"x","password":"123","email":"a@b.com"}` | 400 |
| Email inválido | `{"username":"x","password":"123456","email":"no-email"}` | 400 |
| Login sin campos | `{}` | 400 |
| Token vacío | `/api/auth/verify?token=` | 400 |
| Token falso | `/api/auth/verify?token=abc` | 200 → `false` |
| Credenciales malas | login con password incorrecto | 401 |
| Usuario duplicado | repetir register con mismo username | 409 |

### 1.2 ms-clientes (8086)

**Cuentas corporativas** — `/api/corporate-accounts`

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| POST | `/api/corporate-accounts` | `{"ruc":"1790012345001","businessName":"ESPE Logistica","creditLimit":10000,"industry":"Educacion"}` | 201 |
| GET | `/api/corporate-accounts` | — | 200 |
| GET | `/api/corporate-accounts/{id}` | — | 200 |
| PUT | `/api/corporate-accounts/{id}` | igual que POST | 200 |
| DELETE | `/api/corporate-accounts/{id}` | — | 204 |

**Clientes** — `/api/clients`

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| POST | `/api/clients` | `{"firstName":"Ana","lastName":"Paredes","email":"ana@example.com","phone":"0999999999","address":"Sangolqui","corporateAccountId":1}` | 201 |
| GET | `/api/clients` | — | 200 |
| GET | `/api/clients/{id}` | — | 200 |
| PUT | `/api/clients/{id}` | igual que POST | 200 |
| DELETE | `/api/clients/{id}` | — | 204 |

**Negativos:**

| Caso | Espera |
| --- | ---: |
| `GET /api/clients/-1` | 400 |
| `GET /api/clients/abc` | 400 |
| `GET /api/clients/99999` (no existe) | 404 |
| RUC con menos de 13 dígitos (`"123"`) | 400 |
| `creditLimit` negativo o ausente | 400 |
| Cliente sin `firstName`/`email` | 400 |
| `corporateAccountId: -5` | 400 |

### 1.3 ms-flota-rest (8081)

**Vehículos** — `/api/vehicles` · tipos: `Moto`, `Auto`, `Furgoneta`, `Camion` · estados: `AVAILABLE`, `BUSY`, `MAINTENANCE`, `INACTIVE`

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| POST | `/api/vehicles` | `{"plate":"PBA-2040","type":"Furgoneta","capacityKg":1200,"autonomyKm":450,"status":"AVAILABLE"}` | 201 |
| GET | `/api/vehicles` | — | 200 |
| GET | `/api/vehicles/available` | — | 200 |
| GET | `/api/vehicles/{uuid}` | — | 200 |
| PUT | `/api/vehicles/{uuid}` | igual que POST | 200 |
| DELETE | `/api/vehicles/{uuid}` | — | 204 |

**Conductores** — `/api/drivers` (`licenseNumber` = cédula ecuatoriana válida)

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| POST | `/api/drivers` | `{"firstName":"Luis","lastName":"Tipan","licenseNumber":"1710034065","phone":"0991112233","available":true}` | 201 |
| GET | `/api/drivers` · `/api/drivers/available` · `/api/drivers/{uuid}` | — | 200 |
| PUT | `/api/drivers/{uuid}` | igual que POST | 200 |
| DELETE | `/api/drivers/{uuid}` | — | 204 |

**Disponibilidad y mantenimiento (proxy a taller):**

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| GET | `/api/fleet/availability` | — | 200 (vehículos + conductores libres) |
| GET | `/api/vehicles/{uuid}/maintenance` | — | 200 (consulta a ms-taller) |
| POST | `/api/vehicles/{uuid}/maintenance-orders` | `{"descripcion":"Cambio de aceite y frenos"}` | 201 |

**Negativos:**

| Caso | Espera |
| --- | ---: |
| `type` fuera de la lista (`"Avion"`) | 400 |
| `capacityKg` negativo o > 50000 | 400 |
| Cédula inválida (`"0000000000"`) | 400 |
| `GET /api/vehicles/no-es-uuid` | 400 |
| Matrícula duplicada | 409 |
| UUID inexistente | 404 |

### 1.4 ms-taller (8089)

Disponible con prefijo `/api` y sin prefijo (contrato de la rúbrica).

| Método | URL | Body | Espera |
| --- | --- | --- | ---: |
| GET | `/vehiculos/{matricula}` | — | 200 |
| GET | `/api/vehiculos/{matricula}` | — | 200 |
| POST | `/mantenimientos` | `{"matricula":"PBA-2040","descripcion":"Revision de motor"}` | 201 |
| POST | `/api/mantenimientos` | igual | 201 |

Sin órdenes previas el vehículo responde `estado: DISPONIBLE`; tras registrar una, `EN MANTENIMIENTO`.

**Negativos:** `matricula` de 1 carácter → 400 · `descripcion` en blanco → 400.

### 1.5 ms-pedidos (8087)

`geographicLevel`: `LOCAL`, `PROVINCIAL`, `NATIONAL`.

| Método | URL | Body | Espera | Evento |
| --- | --- | --- | ---: | --- |
| POST | `/api/orders` | `{"clienteId":"1","customerEmail":"ana@example.com","items":["CAJA-001"],"total":25.5,"weightKg":8.0,"geographicLevel":"LOCAL","vehicleType":"Furgoneta","origin":"Sangolqui","destination":"Quito"}` | 201 | **publica `pedido.creado`** |
| GET | `/api/orders` | — | 200 | — |
| GET | `/api/orders/{id}` | — | 200 | — |
| GET | `/api/orders/active/client/1` | — | 200 | — |
| POST | `/api/orders/{id}/cancel` | — | 200 | **publica `pedido.cancelado`** |
| POST | `/api/orders/{id}/deliver` | — | 200 | **publica `pedido.entregado`** |

**Negativos:**

| Caso | Espera |
| --- | ---: |
| `GET /api/orders/-1` | 400 |
| `GET /api/orders/abc` | 400 |
| `GET /api/orders/99999` (no existe) | 404 |
| `items: []` (lista vacía) | 400 |
| `customerEmail` inválido | 400 |
| `total` negativo o ausente | 400 |
| `weightKg` ausente | 400 |
| `geographicLevel` en blanco | 400 |

### 1.6 ms-ruteo (8082)

| Método | URL | Body | Espera | Evento |
| --- | --- | --- | ---: | --- |
| POST | `/api/shipments/assign` | `{"orderId":"1","customerEmail":"ana@example.com","origin":"Sangolqui","destination":"Quito"}` | 201 | **publica `envio.asignado`** |
| GET | `/api/shipments` | — | 200 | — |
| GET | `/api/shipments/{id}` | — | 200 | — |
| GET | `/api/shipments/order/{orderId}` | — | 200 | — |

> `assign` consulta `GET /api/vehicles/available` de **ms-flota-rest**; necesita al menos un vehículo `AVAILABLE`.
> Si el pedido ya tiene envío, devuelve el existente (idempotente). Sin vehículos disponibles → 404.

**Negativos:**

| Caso | Espera |
| --- | ---: |
| `assign` con body `{}` | 400 |
| `assign` con `customerEmail` inválido | 400 |
| `GET /api/shipments/-1` | 400 |
| `GET /api/shipments/abc` | 400 |
| `GET /api/shipments/99999` (no existe) | 404 |

---

## 2. Pruebas RabbitMQ

Exchange topic: **`logiflow.exchange`**. Eventos del dominio:

| Routing key | Productor | Consumidor |
| --- | --- | --- |
| `pedido.creado` | ms-pedidos | ms-ruteo |
| `envio.asignado` | ms-ruteo | ms-pedidos |
| `pedido.cancelado` | ms-pedidos | (futuros) |
| `pedido.entregado` | ms-pedidos | (futuros: facturación) |
| `posicion.actualizada` | dispositivo/simulador | ms-seguimiento + graphql-gateway |

### 2.1 Inspección (Management API, auth `guest`/`guest`)

| Método | URL | Para qué |
| --- | --- | --- |
| GET | `http://localhost:15672/api/overview` | Estado general del broker |
| GET | `http://localhost:15672/api/exchanges` | Ver `logiflow.exchange` |
| GET | `http://localhost:15672/api/queues` | Colas y mensajes encolados |

Colas creadas por los servicios: `logiflow.ruteo.order.created.queue` (bind a `pedido.creado`),
`logiflow.pedidos.shipment.assigned.queue` (bind a `envio.asignado`), y la cola de tracking en seguimiento/gateway.

### 2.2 Publicar `posicion.actualizada` (simular GPS)

`POST http://localhost:15672/api/exchanges/%2F/logiflow.exchange/publish`
Auth Basic `guest:guest`, body:

```json
{
  "properties": {},
  "routing_key": "posicion.actualizada",
  "payload": "{\"shipmentId\":\"1\",\"orderId\":\"1\",\"lat\":-0.3149,\"lng\":-78.4438,\"speed\":42.0,\"eta\":\"2026-06-22T13:00:00-05:00\",\"timestamp\":\"2026-06-22T12:00:00-05:00\"}",
  "payload_encoding": "string"
}
```

Respuesta esperada: `{"routed": true}`. ms-seguimiento la retransmite por WebSocket y el gateway
guarda la última posición (visible luego en `query envio`).

También puedes publicarla desde la pestaña **Exchanges → logiflow.exchange → Publish message** en el panel web.

### 2.3 Evidencia del flujo de eventos (lo que pide la rúbrica)

1. Crea un pedido (REST o GraphQL) → en los logs de **ms-pedidos** verás `Publishing ... pedido.creado`.
2. En **ms-ruteo**: `Evento pedido.creado recibido. Iniciando auto-asignación`.
3. En **ms-ruteo**: `Publishing shipment.assigned ... envio.asignado`.
4. En **ms-pedidos**: `Evento envio.asignado recibido` → el pedido pasa a `ASSIGNED`.
5. Publica `posicion.actualizada` → **ms-seguimiento**: `Retransmitiendo posición`.

Esto demuestra mensajes fluyendo entre 3+ servicios (pedidos ↔ ruteo ↔ seguimiento).

---

## 3. Pruebas GraphQL (gateway 8080)

Endpoint: `POST http://localhost:8080/graphql` · Playground: http://localhost:8080/graphiql

### 3.1 Mutation `crearPedido`

```graphql
mutation Crear($input: PedidoInput!) {
  crearPedido(input: $input) {
    id clienteId status origin destination total
  }
}
```

Variables:

```json
{
  "input": {
    "clienteId": "1",
    "customerEmail": "ana@example.com",
    "items": ["CAJA-001"],
    "total": 25.5,
    "weightKg": 8.0,
    "geographicLevel": "LOCAL",
    "vehicleType": "Furgoneta",
    "origin": "Sangolqui",
    "destination": "Quito"
  }
}
```

El gateway llama internamente al REST de ms-pedidos (que publica `pedido.creado`).

### 3.2 Query `pedidosActivos`

```graphql
query {
  pedidosActivos(clienteId: "1") {
    id clienteId status origin destination
  }
}
```

### 3.3 Query `envio` (datos agregados + última posición)

```graphql
query Envio($id: ID!) {
  envio(id: $id) {
    id orderId vehiclePlate status origin destination
    lastPosition { lat lng speed eta timestamp }
  }
}
```

Variables: `{ "id": "1" }` (el `id` del envío). `lastPosition` se llena tras publicar
`posicion.actualizada` (sección 2.2); si no, llega `null`.

### 3.4 Mutation `cancelarPedido`

```graphql
mutation Cancelar($id: ID!) {
  cancelarPedido(id: $id) { id status }
}
```

Variables: `{ "id": "1" }`.

### 3.5 Negativos GraphQL

| Caso | Resultado |
| --- | --- |
| Falta un campo `!` del `PedidoInput` (ej. sin `origin`) | error de validación del esquema |
| `crearPedido` con tipo erróneo (`total: "abc"`) | error de coerción |
| `envio(id: "99999")` (no existe) | `errors` con fallo del cliente REST |

> Las *subscriptions* en tiempo real son opcionales en Fase 2 (no implementadas); el tracking en
> vivo se prueba por WebSocket en ms-seguimiento (sección 4).

---

## 4. Flujo end-to-end recomendado (demo)

Orden sugerido (es justo el de las carpetas de la colección Postman):

1. **auth**: register → login (guarda el JWT).
2. **clientes**: crear cuenta corporativa → crear cliente.
3. **flota**: crear vehículo `AVAILABLE` → crear conductor.
4. **taller**: registrar/consultar mantenimiento (opcional, vía flota).
5. **pedidos**: crear pedido → se publica `pedido.creado`.
6. **ruteo**: verificar que el envío se auto-asignó (`GET /api/shipments`) o asignar manualmente.
7. **seguimiento**: suscribirse por STOMP a `/topic/shipment/{id}` (cliente WebSocket) y publicar
   `posicion.actualizada` en RabbitMQ → ver el mensaje llegar en vivo.
8. **graphql**: `query envio(id)` con `lastPosition`, `pedidosActivos`, y `cancelarPedido`.

### WebSocket (ms-seguimiento, 8083)

- Handshake SockJS (chequeo HTTP): `GET http://localhost:8083/ws-tracking/info` → 200.
- Conexión real: cliente STOMP sobre SockJS a `http://localhost:8083/ws-tracking`,
  suscrito a `/topic/shipment/{shipmentId}` y `/topic/order/{orderId}`.
- Postman no maneja STOMP/SockJS directamente; usa un cliente STOMP (navegador con `@stomp/stompjs`,
  o la consola de pruebas del frontend de Fase 3).

---

## 5. Apéndice — JSONs listos para copiar

### ms-auth

Register:
```json
{ "username": "operador", "password": "LogiFlow-2026", "email": "operador@logiflow.ec", "roles": ["OPERATOR"] }
```
Login:
```json
{ "username": "operador", "password": "LogiFlow-2026" }
```

### ms-clientes

Cuenta corporativa (POST/PUT):
```json
{ "ruc": "1790012345001", "businessName": "ESPE Logistica", "creditLimit": 10000, "industry": "Educacion" }
```
Cliente (POST/PUT):
```json
{ "firstName": "Ana", "lastName": "Paredes", "email": "ana@example.com", "phone": "0999999999", "address": "Sangolqui", "corporateAccountId": 1 }
```

### ms-flota-rest

Vehículo (POST/PUT):
```json
{ "plate": "PBA-2040", "type": "Furgoneta", "capacityKg": 1200, "autonomyKm": 450, "status": "AVAILABLE" }
```
Conductor (POST/PUT) — `licenseNumber` es una cédula ecuatoriana válida:
```json
{ "firstName": "Luis", "lastName": "Tipan", "licenseNumber": "1710034065", "phone": "0991112233", "available": true }
```
Orden de mantenimiento (POST `/api/vehicles/{uuid}/maintenance-orders`):
```json
{ "descripcion": "Cambio de aceite y revision de frenos" }
```

### ms-taller

Registrar mantenimiento (POST `/mantenimientos` o `/api/mantenimientos`):
```json
{ "matricula": "PBA-2040", "descripcion": "Revision general de motor" }
```

### ms-pedidos

Crear pedido (POST `/api/orders`):
```json
{ "clienteId": "1", "customerEmail": "ana@example.com", "items": ["CAJA-001"], "total": 25.5, "weightKg": 8.0, "geographicLevel": "LOCAL", "vehicleType": "Furgoneta", "origin": "Sangolqui", "destination": "Quito" }
```

### ms-ruteo

Asignar envío (POST `/api/shipments/assign`):
```json
{ "orderId": "1", "customerEmail": "ana@example.com", "origin": "Sangolqui", "destination": "Quito" }
```

### RabbitMQ — publicar posición

Body para `POST /api/exchanges/%2F/logiflow.exchange/publish` (Basic `guest:guest`):
```json
{ "properties": {}, "routing_key": "posicion.actualizada", "payload": "{\"shipmentId\":\"1\",\"orderId\":\"1\",\"lat\":-0.3149,\"lng\":-78.4438,\"speed\":42.0,\"eta\":\"2026-06-22T13:00:00-05:00\",\"timestamp\":\"2026-06-22T12:00:00-05:00\"}", "payload_encoding": "string" }
```

### GraphQL — bodies HTTP crudos

Para `POST http://localhost:8080/graphql` con `Content-Type: application/json` (en GraphiQL pega solo
la `query` y las `variables` por separado).

crearPedido:
```json
{ "query": "mutation Crear($input: PedidoInput!) { crearPedido(input: $input) { id clienteId status origin destination total } }", "variables": { "input": { "clienteId": "1", "customerEmail": "ana@example.com", "items": ["CAJA-001"], "total": 25.5, "weightKg": 8.0, "geographicLevel": "LOCAL", "vehicleType": "Furgoneta", "origin": "Sangolqui", "destination": "Quito" } } }
```
pedidosActivos:
```json
{ "query": "query { pedidosActivos(clienteId: \"1\") { id clienteId status origin destination } }" }
```
envio:
```json
{ "query": "query Envio($id: ID!) { envio(id: $id) { id orderId vehiclePlate status lastPosition { lat lng speed eta timestamp } } }", "variables": { "id": "1" } }
```
cancelarPedido:
```json
{ "query": "mutation Cancelar($id: ID!) { cancelarPedido(id: $id) { id status } }", "variables": { "id": "1" } }
```

### Ejemplos INVÁLIDOS (para forzar 400)

Pedido con múltiples errores:
```json
{ "clienteId": "", "customerEmail": "no-email", "items": [], "total": -5, "weightKg": 0, "geographicLevel": "", "vehicleType": "", "origin": "", "destination": "" }
```
Cuenta corporativa con RUC mal formado:
```json
{ "ruc": "123", "businessName": "X", "creditLimit": -100 }
```
Vehículo con tipo y capacidad inválidos:
```json
{ "plate": "", "type": "Avion", "capacityKg": -50, "status": "AVAILABLE" }
```
Conductor con cédula inválida:
```json
{ "firstName": "Test", "lastName": "Malo", "licenseNumber": "0000000000", "available": true }
```

---

## 6. Modo Docker (alternativa: todo en contenedores)

Todo el proyecto puede levantarse con Docker (microservicios + 6 PostgreSQL + RabbitMQ + Nginx):

```bash
docker compose up -d --build
docker compose ps
```

- **Punto de entrada unificado:** http://localhost (Nginx en el puerto 80) — ej. `http://localhost/api/orders`,
  `http://localhost/graphql`, `ws://localhost/ws-tracking`.
- Cada servicio **también** publica su puerto directo (8081–8089, 8080), así que **esta misma guía y la
  colección Postman funcionan igual** apuntando a `localhost:<puerto>`.
- Las bases son contenedores (`db-*`), **independientes de tu PostgreSQL local**: los datos NO se comparten
  entre el modo local y el modo Docker.
- Detener: `docker compose down` (agrega `-v` solo si quieres borrar los datos de los volúmenes).

