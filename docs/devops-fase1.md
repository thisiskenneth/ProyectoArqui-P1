# DevOps Fase 1 - LogiFlow

Esta guia deja exactos los pasos externos requeridos para que el pipeline de Fase 1 funcione en GitHub.

## Workflow

Archivo:

- `.github/workflows/backend-ci.yml`

Nombre:

- `Phase 1 CI`

Ramas:

- `main`
- `development`

Eventos:

- `push`
- `pull_request`

Componentes validados:

- `backend/ms-flota-rest`
- `backend/ms-taller-soap`

Componentes excluidos:

- `frontend`
- RabbitMQ
- GraphQL
- WebSockets
- Kubernetes
- servicios de Fase 2 o Fase 3

## Secrets de GitHub Actions

Crear cada secret en:

`GitHub repository > Settings > Secrets and variables > Actions > New repository secret`

### SONAR_TOKEN

- Nombre exacto: `SONAR_TOKEN`
- Valor: token de SonarCloud.
- Donde obtenerlo: `SonarCloud > Account > Security > Generate Tokens`.
- Uso: ejecutar el analisis y consultar metricas para el resumen de Telegram.

### SONAR_PROJECT_KEY

- Nombre exacto: `SONAR_PROJECT_KEY`
- Valor: project key del proyecto SonarCloud.
- Donde obtenerlo: `SonarCloud > Project > Project Information > Project Key`.
- Ejemplo de formato: `usuario_logiflow-fase1`.
- Uso: identificar el proyecto analizado.

### SONAR_ORGANIZATION

- Nombre exacto: `SONAR_ORGANIZATION`
- Valor: organization key de SonarCloud.
- Donde obtenerlo: `SonarCloud > Organization > Administration > Organization key`.
- Uso: asociar el analisis con la organizacion correcta.

### TELEGRAM_BOT_TOKEN

- Nombre exacto: `TELEGRAM_BOT_TOKEN`
- Valor: token del bot de Telegram.
- Donde obtenerlo: hablar con `@BotFather`, ejecutar `/newbot` y copiar el token.
- Formato habitual: `1234567890:AA...`.
- Uso: autenticar el envio del mensaje del pipeline.

### TELEGRAM_CHAT_ID

- Nombre exacto: `TELEGRAM_CHAT_ID`
- Valor: ID del chat o grupo de Telegram.
- Como obtenerlo para chat directo:
  - enviar un mensaje al bot;
  - abrir `https://api.telegram.org/bot<TELEGRAM_BOT_TOKEN>/getUpdates`;
  - copiar `message.chat.id`.
- Como obtenerlo para grupo:
  - agregar el bot al grupo;
  - enviar un mensaje en el grupo;
  - abrir `https://api.telegram.org/bot<TELEGRAM_BOT_TOKEN>/getUpdates`;
  - copiar `message.chat.id`, normalmente inicia con `-`.
- Uso: definir el destino del resumen del pipeline.

## Crear rama development

El PDF exige rama `main` para produccion y `development` para integracion.

Si aun no existe:

```bash
git switch -c development
git push -u origin development
```

Si ya existe localmente:

```bash
git switch development
git push -u origin development
```

Verificacion:

```bash
git branch --all
```

Resultado esperado:

```text
main
development
remotes/origin/main
remotes/origin/development
```

## SonarCloud

Crear un proyecto SonarCloud para este repositorio y usar estos valores en GitHub secrets:

- `SONAR_TOKEN`
- `SONAR_PROJECT_KEY`
- `SONAR_ORGANIZATION`

El archivo `sonar-project.properties` ya define:

- fuentes de `ms-flota-rest`;
- fuentes de `ms-taller-soap`;
- tests de ambos servicios;
- reportes JaCoCo XML;
- exclusion de frontend y componentes fuera de Fase 1.

El pipeline ejecuta tests antes del analisis. Cada servicio genera reporte JaCoCo en:

- `backend/ms-flota-rest/target/site/jacoco/jacoco.xml`
- `backend/ms-taller-soap/target/site/jacoco/jacoco.xml`

## Telegram

El mensaje enviado por el workflow incluye:

- estado final del job;
- workflow;
- job;
- rama;
- SHA corto;
- actor;
- componentes validados;
- resumen SonarCloud con bugs, vulnerabilities, code smells y coverage;
- URL del run.

Si SonarCloud no devuelve metricas todavia, el mensaje usa `N/A` o `SonarCloud summary unavailable`. Esto puede ocurrir en el primer run antes de que SonarCloud termine de indexar el proyecto.

## Validacion local equivalente al pipeline

Ejecutar en PowerShell:

```powershell
Push-Location backend\ms-flota-rest
.\mvnw.cmd test
Pop-Location

Push-Location backend\ms-taller-soap
.\mvnw.cmd test
Pop-Location
```

No ejecutar frontend como parte de Fase 1.
