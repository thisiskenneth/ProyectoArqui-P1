# LogiFlow Backend

Backend distribuido para la plataforma LogiFlow, alineado con los microservicios, APIs, eventos e infraestructura exigidos por el PDF del proyecto.

## Microservicios

| Servicio | API | Responsabilidad |
| --- | --- | --- |
| `ms-auth` | REST | Registro, login y verificacion de JWT. |
| `ms-clientes` | REST | CRUD de clientes y cuentas corporativas. |
| `ms-pedidos` | REST + RabbitMQ | Recepcion y gestion de pedidos; publica eventos de pedidos. |
| `ms-ruteo` | REST + RabbitMQ | Asignacion de pedidos a vehiculos y consulta de envios. |
| `ms-seguimiento` | WebSockets + RabbitMQ | Consume posiciones y las retransmite en tiempo real. No expone REST. |
| `ms-flota-rest` | REST | CRUD de vehiculos/conductores y disponibilidad para ruteo. |
| `ms-taller` | REST | Consulta de vehiculo y registro de ordenes de mantenimiento. |
| `ms-facturacion` | REST + RabbitMQ | Consume pedidos entregados y expone facturas. |
| `ms-notificaciones` | RabbitMQ | Consume eventos y simula notificaciones por logs. No expone API externa. |
| `graphql-gateway` | GraphQL + RabbitMQ | BFF GraphQL para pedidos, envios y ultima posicion conocida. |

## Ejecucion Local

Requisitos:
- JDK 21.
- Docker y Docker Compose.

Levantar todo el backend:

```bash
docker compose up -d --build
```

Ver estado:

```bash
docker compose ps -a
```

Compilar todos los servicios:

```bash
for service in ms-auth ms-clientes ms-flota-rest ms-taller ms-pedidos ms-ruteo ms-seguimiento ms-facturacion ms-notificaciones graphql-gateway; do
  (cd "backend/$service" && ./mvnw -DskipTests compile)
done
```

En Windows PowerShell:

```powershell
$services = 'ms-auth','ms-clientes','ms-flota-rest','ms-taller','ms-pedidos','ms-ruteo','ms-seguimiento','ms-facturacion','ms-notificaciones','graphql-gateway'
foreach ($service in $services) {
  Push-Location "backend\$service"
  .\mvnw.cmd -DskipTests compile
  Pop-Location
}
```

## Puertos Locales

| Servicio | Puerto |
| --- | --- |
| `graphql-gateway` | `8080` |
| `ms-flota-rest` | `8081` |
| `ms-ruteo` | `8082` |
| `ms-seguimiento` | `8083` |
| `ms-facturacion` | `8084` |
| `ms-clientes` | `8086` |
| `ms-pedidos` | `8087` |
| `ms-auth` | `8088` |
| `ms-taller` | `8089` |
| RabbitMQ AMQP | `5672` |
| RabbitMQ Management | `15672` |

`ms-notificaciones` no publica puerto porque el PDF exige que no tenga API externa.

## Endpoints REST

Autenticacion:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/verify?token=...`

Clientes:
- `GET|POST /api/clients`
- `GET|PUT|DELETE /api/clients/{id}`
- `GET|POST /api/corporate-accounts`

Flota:
- `GET|POST /api/vehicles`
- `GET|PUT|DELETE /api/vehicles/{id}`
- `GET /api/vehicles/available`
- `GET|POST /api/drivers`
- `GET|PUT|DELETE /api/drivers/{id}`
- Swagger local: `http://localhost:8081/swagger-ui.html`

Pedidos:
- `GET|POST /api/orders`
- `GET /api/orders/{id}`
- `GET /api/orders/active/client/{clientId}`
- `POST /api/orders/{id}/cancel`
- `POST /api/orders/{id}/deliver`

Ruteo:
- `POST /api/shipments/assign`
- `GET /api/shipments`
- `GET /api/shipments/{id}`
- `GET /api/shipments/order/{orderId}`

Facturacion:
- `GET /api/invoices`
- `GET /api/invoices/{id}`
- `GET /api/invoices/order/{orderId}`

## Taller Mantenimiento

Servicio: `ms-taller`

- Endpoint local: `http://localhost:8089/api/maintenance`
- Operaciones REST:
  - `GET /vehicles/{matricula}`
  - `POST /orders`

## GraphQL

Endpoint local:

```text
http://localhost:8080/graphql
```

Queries:
- `pedidosActivos(clienteId)`
- `envio(id)` con datos del envio y `lastPosition`.

Mutations:
- `crearPedido(input)`
- `cancelarPedido(id)`

## WebSockets

Servicio: `ms-seguimiento`

- Endpoint STOMP/SockJS local: `http://localhost:8083/ws-tracking`
- Topicos:
  - `/topic/shipment/{shipmentId}`
  - `/topic/order/{orderId}`

## Eventos RabbitMQ

Exchange topic:

```text
logiflow.exchange
```

Routing keys:
- `pedido.creado`: publicado por `ms-pedidos`; consumido por `ms-ruteo` y `ms-notificaciones`.
- `pedido.cancelado`: publicado por `ms-pedidos`; consumido por `ms-notificaciones`.
- `pedido.entregado`: publicado por `ms-pedidos`; consumido por `ms-facturacion` y `ms-notificaciones`.
- `envio.asignado`: publicado por `ms-ruteo`; consumido por `ms-pedidos` y `ms-notificaciones`.
- `posicion.actualizada`: publicado por simulador/dispositivos; consumido por `ms-seguimiento` y `graphql-gateway`.

## Kubernetes y API Gateway

Manifiestos:

```text
infrastructure/k8s/
```

Aplicar en un cluster con Nginx Ingress instalado:

```bash
kubectl apply -f infrastructure/k8s/namespace.yaml
kubectl apply -f infrastructure/k8s/rabbitmq.yaml
kubectl apply -f infrastructure/k8s/backend-services.yaml
kubectl apply -f infrastructure/k8s/nginx-ingress.yaml
```

El Ingress `logiflow-gateway` enruta:
- REST: `/api/auth`, `/api/clients`, `/api/corporate-accounts`, `/api/orders`, `/api/shipments`, `/api/vehicles`, `/api/drivers`, `/api/invoices`, `/api/maintenance`.
- GraphQL: `/graphql`, `/graphiql`.
- WebSockets: `/ws-tracking`.

## CI/CD

Workflow:

```text
.github/workflows/backend-ci.yml
```

Se ejecuta en `push` y `pull_request` hacia `main` y `development`.

Configurar estos secretos en GitHub:
- `SONAR_TOKEN`
- `SONAR_PROJECT_KEY`
- `SONAR_ORGANIZATION`
- `TELEGRAM_BOT_TOKEN`
- `TELEGRAM_CHAT_ID`

El pipeline compila todos los microservicios con JDK 21, ejecuta analisis SonarCloud cuando los secretos estan configurados y envia el resultado a Telegram.
