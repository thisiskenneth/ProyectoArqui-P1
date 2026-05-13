Perfecto. Si vas a defenderlo **local sin levantar servicios**, entonces enfócate en **mostrar evidencia en código, docs y configuración**. Tu defensa cambia de “probar en vivo” a “este es el contrato y esta es la implementación”.

**Qué mostrar**

1. **DDD**
Abre [docs/arquitectura-ddd-fase1.md](d:/ESPE/9no%20Semestre/Arquitectura%20de%20Software/1er%20Parcial/Proyecto/docs/arquitectura-ddd-fase1.md).

Muestra estas secciones:

- `Event Storming`
- `Core Domain`, `Subdominios de soporte`, `Subdominios genericos`
- `Bounded Contexts`
- `Lenguaje ubicuo`
- `Context Map`

Frase para defender:

> “Este documento cubre el análisis DDD solicitado: eventos, comandos, agregados, clasificación de dominios, bounded contexts, lenguaje ubicuo y mapa de contexto con patrones de relación.”

2. **ms-flota-rest**
Muestra archivos:

- `backend/ms-flota-rest/src/main/java/.../controller/VehicleController.java`
- `DriverController.java`
- `AvailabilityController.java`
- `OpenApiConfig.java`
- `GlobalExceptionHandler.java`

Frase:

> “Aquí está el microservicio REST de flota. Tiene CRUD completo de vehículos, CRUD completo de conductores, endpoints de disponibilidad para ruteo, documentación OpenAPI y manejo centralizado de errores.”

3. **ms-taller-rest**
Muestra:

- `backend/ms-taller-soap/src/main/java/ec/edu/espe/mstallersoap/controller/MaintenanceRestController.java`
- `MaintenanceService.java`
- `dto/request/MantenimientoRequest.java`
- `dto/response/MantenimientoResponse.java`
- `config/OpenApiConfig.java`

Frase:

> “El taller fue migrado a REST. Expone `GET /vehiculos/{matricula}` y `POST /mantenimientos`, trabaja con JSON, valida datos y publica Swagger/OpenAPI.”

Aunque la carpeta aún se llame `ms-taller-soap`, puedes decir:

> “El nombre de carpeta quedó histórico para no romper rutas del proyecto, pero el artefacto, aplicación y contrato ya están como `ms-taller-rest`.”

4. **DevOps**
Muestra:

- `.github/workflows/backend-ci.yml`
- `sonar-project.properties`
- `README.md`
- `docs/devops-fase1.md`

Frase:

> “El pipeline ejecuta pruebas de ambos servicios, corre análisis con SonarCloud y envía notificación a Telegram. Los secrets requeridos están documentados y validados al inicio del workflow.”

5. **Pruebas**
Muestra tests:

- `VehicleControllerIntegrationTest.java`
- `DriverControllerIntegrationTest.java`
- `AvailabilityControllerIntegrationTest.java`
- `VehicleMaintenanceControllerIntegrationTest.java`
- `MaintenanceRestControllerTest.java`

Frase:

> “La funcionalidad no solo está implementada, también está cubierta con pruebas de integración.”

**Mini guion de defensa**

> “Primero muestro el análisis DDD, que cubre la parte arquitectónica de la rúbrica. Luego paso al microservicio de flota, donde están los controladores REST para vehículos, conductores y disponibilidad. Después muestro el microservicio de taller migrado a REST, con los dos endpoints requeridos. Finalmente enseño la parte DevOps: pipeline, SonarCloud, Telegram, README y configuración de ramas.”

Para cerrar:

> “Con esto se cubren los cuatro bloques de la rúbrica: DDD, flota REST, taller REST y DevOps.”