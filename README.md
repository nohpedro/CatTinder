## CatTinder Platform

### Arquitectura y puertos

| Servicio | Puerto host | Descripción |
| --- | --- | --- |
| `config-server` | 8888 | Spring Cloud Config Server apuntando al repo Git `./config-repo` |
| `eureka-server` | 8761 | Service discovery para los MS |
| `api-gateway` | 9000 | Spring Cloud Gateway (JWT resource-server) que expone `/users`, `/preferences`, `/intereses` y Swagger |
| `users-service` | 8081 | Gestión de usuarios |
| `preferences-service` | 8082 | Preferencias de perfiles |
| `intereses-service` | 8084 | Intereses/afinidades |
| `keycloak` | 8180 (host) / 8080 (container) | Realm `cattinder` con Authorization Code Flow |
| `postgres` | 5432 | Base de datos compartida |

Todos los servicios importan su configuración con `spring.config.import=optional:configserver:${CONFIG_SERVER_URI:http://localhost:8888}`; el gateway es la única puerta de entrada para consumir los microservicios.

### Configuración centralizada (Spring Cloud Config)

- Repo Git de configuración: `./config-repo` (incluido en el proyecto, pero versionado aparte). El Config Server lo monta dentro del contenedor (`./config-repo:/config-repo`).
- `application.yml`: parámetros globales (datasource, JPA, logging, Actuator, Eureka, etc.).
- `<app>.yml`: overrides por servicio (`api-gateway.yml`, `users-service.yml`, etc.).
- **Propiedad cifrada**: `spring.datasource.password` está almacenada como `{cipher}049cfafa...` en el repo. Se generó mediante `POST http://localhost:8888/crypto/encrypt` (Basic `configuser/configpass`). El Config Server la descifra automáticamente y puedes comprobarlo con:

```bash
curl -u configuser:configpass http://localhost:8888/users-service/default | jq '.propertySources[1].source."spring.datasource.password"'
```

- Para añadir/editar valores:
  1. Modifica los `.yml` dentro de `config-repo/`.
  2. `git -C config-repo status`, `git -C config-repo commit -am "mensaje"`.
  3. Reinicia el servicio afectado o refresca la configuración según necesites.

### Arranque con Docker Compose

```powershell
# Windows (powershell)
.\run.ps1              # agrega -Rebuild o -FollowLogs:$false según necesites

# o bien desde cualquier shell
docker compose up -d --build
docker compose ps
docker compose logs -f config-server
```

`run.ps1` levanta primero las dependencias (`postgres`, `keycloak`, `config-server`, `eureka-server`) y después los microservicios + gateway. Para apagar todo: `docker compose down`.

### Evidencias solicitadas

| Criterio | Evidencia en este repo |
| --- | --- |
| Config Server funcional | `curl -u configuser:configpass http://localhost:8888/intereses-service/default` devuelve la configuración (verás `version` del repo Git). |
| Configuración externalizada en Git | Todo vive en `./config-repo` y el Config Server clona desde `file:///config-repo`. Los logs indican `Adding property source: ...config-repo/...yml`. |
| Config general/específica | `config-repo/application.yml` (global) + `<app>.yml` (específico). |
| Config Server en docker-compose | Servicio `config-server` definido en `docker-compose.yml`, con volumen al repo y credenciales propias. Todos los MS dependen de él y reciben `CONFIG_SERVER_*`. |
| Propiedad cifrada | `spring.datasource.password` usa `{cipher}...`. El Config Server expone `/crypto/encrypt` y `/crypto/decrypt` protegidos con Basic auth para generar nuevos valores. |
| Keycloak Realm + Client + Roles | Documentado en `KEYCLOAK_SETUP.md`: realm `cattinder`, roles `ADMIN/USER`, clientes confidenciales por microservicio y cliente público `cattinder-web` con Authorization Code Flow (PKCE). |
| Consumo vía Gateway con token válido | Con un token de Authorization Code, `POST http://localhost:9000/intereses/api/v1/interests` responde 201. Los logs (`docker compose logs -f api-gateway intereses-service`) muestran la petición atravesando el gateway hacia el microservicio. |

### Seguridad y Authorization Code Flow

**Resumen** (ver `KEYCLOAK_SETUP.md` para el detalle completo):

1. Realm `cattinder`.
2. Roles `USER` y `ADMIN`.
3. Clientes:
   - `users-service`, `preferences-service`, `intereses-service`: confidenciales (resource servers).
   - `cattinder-web`: cliente público con *Standard Flow* (Authorization Code) + PKCE. Redirect sugerido para Postman: `https://oauth.pstmn.io/v1/callback`.
4. Usuarios de prueba (`admin`, `user`) con roles asignados.
5. Flujo Authorization Code (ejemplo con Postman):
   - Auth URL: `http://localhost:8180/realms/cattinder/protocol/openid-connect/auth`
   - Token URL: `http://localhost:8180/realms/cattinder/protocol/openid-connect/token`
   - Client ID: `cattinder-web`
   - Scope: `openid profile email`
   - Tras autenticarte, copia `access_token` y úsalo en el gateway.

Los microservicios siguen admitiendo Password Grant/Client Credentials para pruebas automatizadas, pero el criterio principal (Authorization Code) queda documentado y probado vía gateway.

### Probar rápidamente con `requests.http`

El archivo `requests.http` (VS Code / Cursor) apunta al gateway `http://localhost:9000` y contiene:

1. Health checks vía gateway.
2. `POST /intereses/api/v1/interests` sin token (debe retornar 401).
3. Bloque `login` (Password Grant) para reusar tokens en pruebas manuales.
4. `POST` con token para validar tanto gateway como microservicio.

### Gateway y observabilidad

- Rutas configuradas completamente en `config-repo/api-gateway.yml` ¡sin código adicional!
- Seguridad reactiva (`GatewaySecurityConfig`) → todo requiere JWT válido excepto `/actuator/**`, `/swagger/**` y `/v3/api-docs/**`.
- Logging: `RequestLoggingFilter` imprime cada request (`GW -> ...`, `GW <- ...`). Ajusta los niveles desde el archivo de configuración centralizado.

### Desarrollo local sin Docker

1. Deja corriendo Postgres y Keycloak (puedes hacerlo con `docker compose up postgres keycloak-db keycloak`).
2. Lanza el Config Server desde el IDE:
   ```powershell
   $env:CONFIG_GIT_URI="file:///C:/ruta/hacia/CatTinder/config-repo"
   mvn -pl config-server spring-boot:run
   ```
3. Ejecuta cada microservicio/gateway con `mvn spring-boot:run`. No necesitas `application.properties`, sólo el `application.yml` que ya importa el Config Server.
4. Si quieres perfiles distintos, agrega `application-<profile>.yml` dentro de `config-repo/` y exporta `CONFIG_SERVER_PROFILE=<profile>` en el microservicio.

### Archivos importantes

- `config-server/` → microservicio Spring Boot + Dockerfile.
- `config-server/src/main/resources/application.yml` → conexión Git, credenciales, key de cifrado.
- `config-server/src/main/java/com/example/configserver/web/CryptoController.java` → endpoints `/crypto/encrypt` y `/crypto/decrypt`.
- `config-repo/` → configuración externa (Git).
- `docker-compose.yml` → todos los servicios, incluyendo config-server y Keycloak.
- `KEYCLOAK_SETUP.md` → guía completa de realm, clientes y Authorization Code Flow.
- `run.ps1` → script helper en Windows.
- `requests.http` → colección mínima de pruebas end-to-end.

### Troubleshooting

- **Config Server en estado DOWN**: revisa `docker compose logs config-server`. Debe estar limpio (`git -C config-repo status`). Si ves `Could not fetch remote`, ignora (no hay origin) mientras el repo esté limpio.
- **Algún microservicio no arranca**: confirma que `CONFIG_SERVER_URI`, `CONFIG_SERVER_USER` y `CONFIG_SERVER_PASSWORD` están definidos (en Docker Compose ya vienen). El log mostrará `Could not locate PropertySource` si el Config Server no respondió.
- **401/403 en el gateway**: revisa el JWT en `jwt.io`. El issuer debe ser `http://localhost:8180/realms/cattinder` y el rol debe existir en `realm_access.roles`.
- **Necesitas un nuevo secreto cifrado**: mientras el Config Server esté corriendo, ejecuta `curl -s -u configuser:configpass -H "Content-Type: text/plain" -d "<texto>" http://localhost:8888/crypto/encrypt`.
- **Logs de evidencia**: `docker compose logs config-server` muestra `Adding property source ...`, `docker compose logs api-gateway` muestra las peticiones hacia intereses (tal como pide el entregable).

Con esta estructura queda cubierta la configuración centralizada, el cifrado, la orquestación en Docker y el flujo de autorización solicitado.
