# LogiFlow - Fase 1

Repositorio actualizado para la Fase 1 del proyecto LogiFlow: descubrimiento DDD, piloto REST de flota, piloto REST de taller y pipeline DevOps basico.

## Alcance exacto

Incluye como entregables de Fase 1:

- Documento DDD / propuesta de arquitectura: `docs/arquitectura-ddd-fase1.md`.
- Microservicio REST `ms-flota-rest`.
- Microservicio REST `ms-taller-rest`.
- Pipeline GitHub Actions para compilar, probar, analizar con SonarCloud y notificar a Telegram.

No incluye como entregable de Fase 1:

- Frontend.
- GraphQL.
- WebSockets.
- RabbitMQ.
- Kubernetes o Helm.
- API Gateway externo.
- Autenticacion.
- Clientes.
- Pedidos.
- Ruteo ejecutable.
- Seguimiento ejecutable.
- Facturacion.
- Notificaciones.
- Despliegue cloud.

El directorio `frontend/` puede permanecer en el repositorio como trabajo fuera de alcance, pero no participa en `docker-compose.yml`, no se valida en el pipeline de Fase 1 y no cuenta como entregable de esta fase.

## Estructura Fase 1

```text
backend/
  ms-flota-rest/
  ms-taller-soap/
docs/
  arquitectura-ddd-fase1.md
  devops-fase1.md
.github/workflows/backend-ci.yml
docker-compose.yml
sonar-project.properties
README.md
```

## Requisitos locales

- JDK 21.
- Docker y Docker Compose, solo si se desea ejecutar los dos pilotos con contenedores.
- Git.

## Ejecucion local con Docker Compose

```bash
docker compose up -d --build
```

Servicios publicados:

- `ms-flota-rest`: `http://localhost:8081`
- `ms-taller-rest`: `http://localhost:8089`

Detener servicios:

```bash
docker compose down
```

## Ejecucion local sin Docker

Compilar y probar `ms-flota-rest`:

```powershell
Push-Location backend\ms-flota-rest
.\mvnw.cmd test
Pop-Location
```

Ejecutar `ms-flota-rest`:

```powershell
Push-Location backend\ms-flota-rest
.\mvnw.cmd spring-boot:run
Pop-Location
```

Compilar y probar `ms-taller-soap`:

```powershell
Push-Location backend\ms-taller-soap
.\mvnw.cmd test
Pop-Location
```

Ejecutar `ms-taller-soap`:

```powershell
Push-Location backend\ms-taller-soap
.\mvnw.cmd spring-boot:run
Pop-Location
```

## ms-flota-rest

Puerto local: `8081`

Documentacion:

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

Endpoints:

- `GET /api/vehicles`
- `GET /api/vehicles/{id}`
- `POST /api/vehicles`
- `PUT /api/vehicles/{id}`
- `DELETE /api/vehicles/{id}`
- `GET /api/vehicles/available`
- `GET /api/drivers`
- `GET /api/drivers/{id}`
- `POST /api/drivers`
- `PUT /api/drivers/{id}`
- `DELETE /api/drivers/{id}`
- `GET /api/drivers/available`
- `GET /api/fleet/availability`

Este servicio expone solo REST. No contiene GraphQL, WebSockets ni mensajeria.

## ms-taller-rest

Puerto local: `8089`

Documentacion:

- Swagger UI: `http://localhost:8089/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8089/api-docs`

Endpoints:

- `GET /api/vehiculos/{matricula}`
- `POST /api/mantenimientos`
- `GET /vehiculos/{matricula}`
- `POST /mantenimientos`

Este servicio expone solo REST. No contiene GraphQL, WebSockets ni mensajeria.

## Pipeline Fase 1

Workflow: `.github/workflows/backend-ci.yml`

Se ejecuta en:

- `push` a `main`.
- `push` a `development`.
- `pull_request` hacia `main`.
- `pull_request` hacia `development`.

Valida solo:

- `backend/ms-flota-rest`
- `backend/ms-taller-soap`
- `sonar-project.properties`

Pasos del pipeline:

- valida que existan los secrets externos requeridos;
- configura JDK 21;
- ejecuta `./mvnw -B test` en `ms-flota-rest`;
- ejecuta `./mvnw -B test` en `ms-taller-rest`;
- ejecuta SonarCloud con `sonar-project.properties`;
- consulta resumen de SonarCloud: bugs, vulnerabilities, code smells y coverage;
- envia resumen a Telegram.

## Secrets obligatorios en GitHub Actions

Crear en GitHub: `Settings > Secrets and variables > Actions > New repository secret`.

| Secret | Valor exacto esperado | Proposito |
| --- | --- | --- |
| `SONAR_TOKEN` | Token generado en SonarCloud para este proyecto | Permite ejecutar analisis y consultar metricas |
| `SONAR_PROJECT_KEY` | Project Key del proyecto en SonarCloud | Identifica el proyecto analizado |
| `SONAR_ORGANIZATION` | Organization Key de SonarCloud | Identifica la organizacion |
| `TELEGRAM_BOT_TOKEN` | Token del bot creado con BotFather | Permite enviar mensajes a Telegram |
| `TELEGRAM_CHAT_ID` | ID numerico del chat o grupo destino | Define a donde se envia el resumen |

Si falta cualquiera de estos secrets, el pipeline falla al inicio. Esto evita una falsa entrega con SonarCloud o Telegram "opcional".

## Configuracion de SonarCloud

Archivo usado por el pipeline: `sonar-project.properties`.

El archivo analiza solo Fase 1:

- `backend/ms-flota-rest/src/main/java`
- `backend/ms-taller-soap/src/main/java`

Excluye:

- `frontend/**`
- `infrastructure/**`
- `target/**`
- `.idea/**`

La cobertura se toma de JaCoCo:

- `backend/ms-flota-rest/target/site/jacoco/jacoco.xml`
- `backend/ms-taller-soap/target/site/jacoco/jacoco.xml`

## Rama development

El PDF exige `main` y `development`.

Crear y publicar `development` desde el estado actual:

```bash
git switch -c development
git push -u origin development
```

Si la rama ya existe localmente:

```bash
git switch development
git push -u origin development
```

Verificar ramas:

```bash
git branch --all
```

Debe aparecer:

- `main`
- `development`
- `remotes/origin/main`
- `remotes/origin/development`

## Mas detalle DevOps

La guia operativa completa esta en `docs/devops-fase1.md`.
