# LogiFlow - Fase 2

Backend esencial de LogiFlow con REST, GraphQL, RabbitMQ y seguimiento por WebSocket. Conserva los pilotos de Fase 1 y excluye Kubernetes por decisión del proyecto.

## Alcance

| Componente | API | Puerto | Responsabilidad |
| --- | --- | ---: | --- |
| ms-flota-rest | REST | 8081 | Vehículos, conductores y disponibilidad |
| ms-taller-rest | REST heredado de Fase 1 | 8089 | Consulta y mantenimiento |
| ms-auth | REST | 8088 | Registro, login y verificación JWT |
| ms-clientes | REST | 8086 | CRUD de clientes y cuentas corporativas |
| ms-pedidos | REST + eventos | 8087 | Gestión de pedidos y eventos |
| ms-ruteo | REST + eventos | 8082 | Asignación y consulta de envíos |
| ms-seguimiento | WebSocket solamente | 8083 | Retransmisión de posiciones STOMP |
| graphql-gateway | GraphQL | 8080 | BFF de pedidos y envíos |
| RabbitMQ | AMQP / Management | 5672 / 15672 | Bus topic de eventos |
| Nginx | HTTP / WebSocket | 80 | Entrada unificada sin Kubernetes |

No se incluyen manifiestos Kubernetes, Helm, Minikube, Kind ni K3s. Facturación, notificaciones y frontend completo pertenecen a Fase 3.

## Requisitos

- JDK 21 (incluido el wrapper Maven `mvnw`).
- PostgreSQL local en `localhost:5432` (usuario `postgres`, contraseña `1234`) para la ejecución local de los microservicios.
- Docker Desktop para levantar RabbitMQ (y, opcionalmente, la integración completa).
- IntelliJ IDEA (opcional, recomendado para ejecutar los servicios).
- Git.

## Ejecución local (microservicios locales + PostgreSQL local + RabbitMQ en Docker)

En esta modalidad los **microservicios corren en local** (IntelliJ o `mvnw`), la **base de datos es tu PostgreSQL local** y solo **RabbitMQ se ejecuta en Docker**. Cada microservicio con persistencia usa su propia base (database-per-service):

| Servicio | Puerto | Base PostgreSQL | Bus |
| --- | ---: | --- | --- |
| ms-flota-rest | 8081 | `flotadb` | - |
| ms-taller-soap | 8089 | `tallerdb` | - |
| ms-auth | 8088 | `authdb` | - |
| ms-clientes | 8086 | `clientesdb` | - |
| ms-pedidos | 8087 | `pedidosdb` | RabbitMQ |
| ms-ruteo | 8082 | `ruteodb` | RabbitMQ |
| ms-seguimiento | 8083 | H2 en memoria (relay efímero) | RabbitMQ |
| graphql-gateway | 8080 | sin base | RabbitMQ |

### 1. Crear las bases en PostgreSQL local

~~~powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -h localhost -p 5432 -f infrastructure\local\create-databases.sql
~~~

(Ajusta la ruta de `psql.exe` a tu versión. Las credenciales por defecto son `postgres` / `1234`; cámbialas con las variables de entorno `DB_USERNAME` y `DB_PASSWORD` si tu instalación usa otras.)

### 2. Levantar solo RabbitMQ en Docker

~~~powershell
docker compose up -d rabbitmq
~~~

RabbitMQ Management queda en http://localhost:15672 (guest / guest).

### 3. Ejecutar los microservicios desde IntelliJ

1. `File > Open` y selecciona `backend/pom.xml` (es un POM agregador que importa los 8 microservicios como un único proyecto multimódulo).
2. Espera a que IntelliJ resuelva las dependencias Maven.
3. Ejecuta cada servicio con el botón ▶ sobre su clase `*Application` (por ejemplo `MsPedidosApplication`). El plugin de Spring Boot crea la configuración de ejecución automáticamente.

Los valores por defecto ya apuntan a `localhost:5432` (PostgreSQL) y `localhost:5672` (RabbitMQ), por lo que no necesitas configurar variables de entorno.

### 4. Alternativa por terminal (sin IntelliJ)

~~~powershell
# en una terminal por servicio
Push-Location backend\ms-pedidos ; .\mvnw.cmd spring-boot:run ; Pop-Location
~~~

## Pruebas (sin PostgreSQL ni RabbitMQ)

Las pruebas usan el perfil `test` (H2 en memoria) y no requieren PostgreSQL ni RabbitMQ, igual que en CI:

~~~powershell
$services = 'ms-flota-rest','ms-taller-soap','ms-auth','ms-clientes','ms-pedidos','ms-ruteo','ms-seguimiento','graphql-gateway'
foreach ($service in $services) {
  Push-Location "backend\$service"
  .\mvnw.cmd test
  Pop-Location
}
~~~

## Arranque integrado

Después de activar Docker:

~~~powershell
docker compose up -d --build
docker compose ps
~~~

Entrada unificada: http://localhost. RabbitMQ Management: http://localhost:15672 con guest / guest.

Para detener: docker compose down. Los datos PostgreSQL se conservan en volúmenes; no use docker compose down -v en una entrega normal.

## Contratos principales

REST:

- Auth: POST /api/auth/register, POST /api/auth/login y POST /api/auth/verify?token=...
- Clientes: CRUD en /api/clients y /api/corporate-accounts.
- Pedidos: /api/orders; cancelación en POST /api/orders/{id}/cancel.
- Ruteo: POST /api/shipments/assign, GET /api/shipments y GET /api/shipments/{id}.
- Swagger directo: http://localhost:<puerto>/swagger-ui.html.

GraphQL:

- UI: http://localhost:8080/graphiql.
- Endpoint unificado: POST http://localhost/graphql.
- Queries: pedidosActivos(clienteId) y envio(id).
- Mutations: crearPedido(input) y cancelarPedido(id).

WebSocket STOMP/SockJS:

- Handshake: http://localhost/ws-tracking.
- Tópicos: /topic/shipment/{shipmentId} y /topic/order/{orderId}.
- ms-seguimiento no declara controladores REST.

## Eventos RabbitMQ

Exchange topic: logiflow.exchange.

| Routing key | Productor | Consumidor |
| --- | --- | --- |
| pedido.creado | ms-pedidos | ms-ruteo |
| pedido.cancelado | ms-pedidos | consumidores futuros |
| envio.asignado | ms-ruteo | ms-pedidos |
| posicion.actualizada | dispositivo/simulador | ms-seguimiento y graphql-gateway |

La arquitectura y el procedimiento de evidencia están en docs/arquitectura-fase2.md. Las solicitudes reproducibles están en test-endpoints.http.

## CI/CD

.github/workflows/backend-ci.yml conserva main y development, prueba los ocho servicios, analiza todos los módulos con SonarCloud y notifica a Telegram. Usa los mismos secrets de docs/devops-fase1.md.