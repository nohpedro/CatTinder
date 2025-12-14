# CatTinder - Configuraciones Centralizadas

Este repositorio contiene las configuraciones centralizadas para todos los microservicios de CatTinder usando Spring Cloud Config.

## Servicios Configurados

- **gateway-service**: API Gateway con rutas y CORS
- **swipe-service**: Servicio de swipes con base de datos PostgreSQL
- **authentication-service**: Servicio de autenticación con OAuth2/Keycloak
- **eureka-server**: Servicio de registro y descubrimiento

## Configuraciones Importantes

### Keycloak OAuth2
Todos los servicios están configurados como Resource Servers con:
- **Issuer URI**: `http://localhost:8090/realms/swipes-realm`
- **JWK Set URI**: `http://keycloak:8090/realms/swipes-realm/protocol/openid-connect/certs`

### Base de Datos
- **PostgreSQL**: `postgres:5432/review-db`
- **Usuario**: `admin`
- **Contraseña**: `admin`

### Eureka
- **URL**: `http://eureka:8762/eureka/`
- Todos los servicios se registran automáticamente

## Cómo Usar

1. **Crear repositorio en GitHub** con estos archivos
2. **Actualizar docker-compose.yml**:
   ```yaml
   SPRING_CLOUD_CONFIG_SERVER_GIT_URI: "https://github.com/TU_USUARIO/TU_REPO_CONFIG"
   ```
3. **Levantar servicios**: `docker-compose up`

## Verificación

- **Eureka Dashboard**: `http://localhost:8762`
- **Keycloak Admin**: `http://localhost:8090`
- **Gateway**: `http://localhost:8082`

¡Todos los servicios estarán conectados y funcionando en localhost!

