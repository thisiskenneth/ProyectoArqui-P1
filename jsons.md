Claro. Si vas a defender con Swagger, este es el guion práctico.

**URLs Swagger**

```text
Flota REST:
http://localhost:8081/swagger-ui.html

Taller REST:
http://localhost:8089/swagger-ui.html
```

**1. Flota REST**

En `http://localhost:8081/swagger-ui.html`, muestra estos grupos:

**vehicle-controller**

Defiendes CRUD completo de vehículos:

```text
POST /api/vehicles
GET /api/vehicles
GET /api/vehicles/{id}
PUT /api/vehicles/{id}
DELETE /api/vehicles/{id}
GET /api/vehicles/available
```

Ejemplo para `POST /api/vehicles`:

```json
{
  "plate": "ABC-1234",
  "type": "Camion",
  "capacityKg": 2500,
  "autonomyKm": 450,
  "status": "AVAILABLE"
}
```

Frase:

> “Aquí está el CRUD completo de vehículos. Maneja matrícula, tipo, capacidad, autonomía y estado. Además, `/api/vehicles/available` permite consultar vehículos disponibles para ruteo.”

**driver-controller**

Defiendes CRUD completo de conductores:

```text
POST /api/drivers
GET /api/drivers
GET /api/drivers/{id}
PUT /api/drivers/{id}
DELETE /api/drivers/{id}
GET /api/drivers/available
```

Ejemplo para `POST /api/drivers`:

```json
{
  "firstName": "Juan",
  "lastName": "Perez",
  "licenseNumber": "1710034065",
  "phone": "0999999999",
  "available": true
}
```

Frase:

> “Aquí está el CRUD completo de conductores y también la consulta de conductores disponibles.”

**availability-controller**

Defiendes disponibilidad para ruteo:

```text
GET /api/fleet/availability
```

Frase:

> “Este endpoint consolida la disponibilidad de flota para que un futuro módulo de ruteo pueda consumir vehículos y conductores disponibles.”

**vehicle-maintenance-controller**

Defiendes integración con taller:

```text
GET /api/vehicles/{id}/maintenance
POST /api/vehicles/{id}/maintenance-orders
```

Ejemplo para `POST /api/vehicles/{id}/maintenance-orders`:

```json
{
  "descripcion": "Revision preventiva"
}
```

Frase:

> “Estos endpoints muestran la integración REST entre flota y taller. Flota consulta mantenimiento y registra órdenes usando el servicio de taller.”

**2. Taller REST**

En `http://localhost:8089/swagger-ui.html`, muestra el controlador de mantenimiento.

Endpoints principales de la rúbrica:

```text
GET /vehiculos/{matricula}
POST /mantenimientos
```

También pueden aparecer aliases:

```text
GET /api/vehiculos/{matricula}
POST /api/mantenimientos
```

Para defender la rúbrica, usa los que no tienen `/api`.

**GET /vehiculos/{matricula}**

Ejemplo:

```text
ABC-1234
```

Frase:

> “Este endpoint equivale a consultar vehículo. Devuelve matrícula, estado, último mantenimiento y observaciones.”

**POST /mantenimientos**

Body:

```json
{
  "matricula": "ABC-1234",
  "descripcion": "Cambio de aceite"
}
```

Frase:

> “Este endpoint registra una orden de mantenimiento en formato JSON y devuelve código de orden, fecha de ingreso y mensaje.”

**Secuencia recomendada para la demo**

1. En taller, ejecuta:

```text
GET /vehiculos/ABC-1234
```

Muestras que inicialmente puede salir disponible o sin registro.

2. Ejecuta:

```text
POST /mantenimientos
```

Con:

```json
{
  "matricula": "ABC-1234",
  "descripcion": "Cambio de aceite"
}
```

3. Ejecuta de nuevo:

```text
GET /vehiculos/ABC-1234
```

Muestras que ahora ya aparece con orden de mantenimiento.

**Frase para Swagger/OpenAPI**

> “Swagger demuestra que el contrato REST está documentado. Aquí se pueden ver métodos HTTP, rutas, parámetros, bodies JSON, modelos de respuesta y códigos HTTP. Además permite probar los endpoints con `Try it out`, lo que evidencia que la API es consumible y clara.”
