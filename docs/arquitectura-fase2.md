# Arquitectura Fase 2 sin Kubernetes

## Decisión de despliegue

La entrega implementa todos los componentes funcionales de la Fase 2 excepto Kubernetes. Docker Compose proporciona descubrimiento por nombre de servicio, redes internas, bases independientes, RabbitMQ y un punto de entrada Nginx. No existen manifiestos YAML de Kubernetes ni charts Helm.

## Diagrama de componentes

~~~mermaid
flowchart LR
    U[Consumidor frontend] --> N[Nginx API Gateway]
    N --> A[ms-auth REST]
    N --> C[ms-clientes REST]
    N --> F[ms-flota-rest REST]
    N --> T[ms-taller-rest]
    N --> P[ms-pedidos REST]
    N --> R[ms-ruteo REST]
    N --> G[graphql-gateway GraphQL]
    N --> S[ms-seguimiento WebSocket]

    G --> P
    G --> R
    R --> F

    P -- pedido.creado --> MQ[(RabbitMQ topic)]
    P -- pedido.cancelado --> MQ
    MQ -- pedido.creado --> R
    R -- envio.asignado --> MQ
    MQ -- envio.asignado --> P
    SIM[Simulador o dispositivo] -- posicion.actualizada --> MQ
    MQ -- posicion.actualizada --> S
    MQ -- posicion.actualizada --> G
    S -- STOMP topics --> U

    A --> DBA[(authdb)]
    C --> DBC[(clientesdb)]
    P --> DBP[(pedidosdb)]
    R --> DBR[(ruteodb)]
    F --> DBF[(flotadb)]
    T --> DBT[(tallerdb)]
~~~

## Límites de API

- ms-auth, ms-clientes, ms-pedidos, ms-ruteo y ms-flota-rest exponen REST.
- graphql-gateway es el único componente que expone GraphQL.
- ms-seguimiento no contiene controladores REST; consume RabbitMQ y publica STOMP/WebSocket.
- RabbitMQ usa un único exchange topic llamado logiflow.exchange con colas propias por consumidor.
- Cada servicio con estado de negocio tiene una base PostgreSQL separada en Docker Compose.

## Flujo de pedido y asignación

1. El consumidor ejecuta crearPedido por GraphQL o POST /api/orders.
2. graphql-gateway delega por REST a ms-pedidos.
3. ms-pedidos persiste el pedido y publica pedido.creado.
4. La cola logiflow.ruteo.order.created.queue entrega el evento a ms-ruteo.
5. ms-ruteo consulta los vehículos disponibles de ms-flota-rest.
6. El algoritmo simple elige el primer vehículo disponible y persiste el envío.
7. ms-ruteo publica envio.asignado.
8. ms-pedidos consume el evento y cambia el pedido a ASSIGNED.

Los identificadores de vehículos se manejan como String en ruteo para aceptar los UUID producidos por la Fase 1.

## Flujo de posición en tiempo real

1. Un dispositivo o simulador publica posicion.actualizada en logiflow.exchange.
2. ms-seguimiento consume logiflow.tracking.queue.
3. El evento se retransmite a /topic/shipment/{shipmentId} y /topic/order/{orderId}.
4. graphql-gateway consume su propia cola y conserva la última posición para la query envio(id).

Las colas separadas evitan competir por el mismo mensaje: seguimiento y gateway reciben una copia.

## Evidencia funcional después de activar Docker

1. Ejecutar docker compose up -d --build.
2. Ejecutar en orden las solicitudes de test-endpoints.http.
3. Abrir RabbitMQ Management en http://localhost:15672.
4. Verificar el exchange logiflow.exchange y estas colas:
   - logiflow.ruteo.order.created.queue
   - logiflow.pedidos.shipment.assigned.queue
   - logiflow.tracking.queue
   - logiflow.gateway.tracking.queue
5. Consultar GET /api/shipments y comprobar el envío creado automáticamente.
6. Suscribirse por STOMP al tópico del envío y publicar la solicitud Posición del archivo HTTP.
7. Capturar la pantalla de Queues y el mensaje recibido como evidencia de los tres servicios.

## Persistencia

Docker Compose crea volúmenes distintos para flota, taller, auth, clientes, pedidos y ruteo. H2 queda como fallback aislado para pruebas Maven sin Docker. ms-seguimiento no persiste posiciones; su responsabilidad en Fase 2 es exclusivamente la retransmisión en tiempo real.

## Seguridad JWT

ms-auth usa BCrypt para contraseñas y una clave JWT configurable mediante JWT_SECRET. La contraseña se excluye de la serialización JSON. En una instalación real se debe reemplazar el valor de desarrollo definido en Docker Compose.

## DevOps

El workflow Phase 2 CI compila y prueba los ocho módulos backend. SonarCloud recibe fuentes, binarios y reportes JaCoCo de todos ellos. El paso final mantiene la notificación Telegram de la Fase 1 con el inventario actualizado.