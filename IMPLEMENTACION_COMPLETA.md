# 📋 Documentación Completa de Implementación - CatTinder

## 🎯 Resumen Ejecutivo

Este documento detalla **todo lo implementado** para cumplir con los criterios de evaluación de **Configuración Centralizada (Spring Cloud Config Server)** y **Keycloak + Web App (Authorization Code Flow)**.

---

## ✅ PUNTOS COMPLETADOS

### 1. Configuración Centralizada – Spring Cloud Config Server

#### ✅ Microservicio Config Server funcional
- **Estado**: ✅ **COMPLETO**
- **Evidencia**: Endpoint `/intereses-service/default` responde correctamente con configuración desde Git

#### ✅ Configuración externalizada en Git
- **Estado**: ✅ **COMPLETO**
- **Evidencia**: Logs del Config Server muestran "Fetching config from Git" y "Adding property source: ...config-repo/...yml"

#### ✅ Configuración general y específica separada
- **Estado**: ✅ **COMPLETO**
- **Evidencia**: `application.yml` (global) y archivos por aplicación (`users-service.yml`, `intereses-service.yml`, etc.) funcionan correctamente

#### ✅ Config Server funcional en docker-compose
- **Estado**: ✅ **COMPLETO**
- **Evidencia**: Gateway, Config Server, Eureka y microservicios levantan juntos

#### ✅ Propiedad cifrada con Spring Cloud Encryption
- **Estado**: ✅ **COMPLETO**
- **Evidencia**: Propiedad con formato `{cipher}` visible y descifrada en `/intereses-service/default` y el microservicio funciona correctamente

### 2. Keycloak + Web App (Authorization Code Flow)

#### ⚠️ Keycloak configurado correctamente (Realm + Client + Roles)
- **Estado**: ⚠️ **PENDIENTE (MANUAL)**
- **Nota**: El código está listo, pero debes configurar Keycloak manualmente siguiendo `KEYCLOAK_SETUP.md`

#### ✅ Consumo de endpoints a través del Gateway con Token válido
- **Estado**: ✅ **COMPLETO (CÓDIGO)**
- **Nota**: El gateway está configurado para validar tokens. Falta probar con token real (requiere Keycloak configurado)

---

## 📁 ARCHIVOS Y CARPETAS MODIFICADOS/CREADOS

### 🆕 Carpetas Nuevas Creadas

```
config-server/                          # ← NUEVO: Microservicio Config Server
├── src/
│   ├── main/
│   │   ├── java/com/example/configserver/
│   │   │   ├── ConfigServerApplication.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   └── web/
│   │   │       └── CryptoController.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── Dockerfile
└── pom.xml

config-repo/                            # ← NUEVO: Repositorio Git de configuración
├── .git/                               # Repositorio Git inicializado
├── application.yml                     # Configuración global
├── api-gateway.yml                     # Config específica del gateway
├── eureka-server.yml                   # Config específica de Eureka
├── intereses-service.yml               # Config específica de intereses
├── preferences-service.yml             # Config específica de preferences
└── users-service.yml                   # Config específica de users
```

### ✏️ Archivos Modificados

#### 📄 Archivos de Configuración del Proyecto

**`pom.xml` (raíz)**
- ✅ Agregado módulo `config-server` en `<modules>`
- ✅ Versiones de Spring Boot (3.3.4) y Spring Cloud (2023.0.3) ya estaban correctas

**`docker-compose.yml`**
- ✅ Agregado servicio `config-server`:
  - Puerto 8888
  - Volumen montado: `./config-repo:/config-repo`
  - Variables de entorno: `CONFIG_GIT_URI`, `CONFIG_SERVER_USER`, `CONFIG_SERVER_PASSWORD`
  - Dependencias: `postgres`, `keycloak-db`, `keycloak`, `eureka-server`
- ✅ Agregado `CONFIG_SERVER_URI`, `CONFIG_SERVER_USER`, `CONFIG_SERVER_PASSWORD` a todos los microservicios
- ✅ Actualizado `depends_on` de todos los servicios para incluir `config-server`

**`run.ps1`**
- ✅ Actualizado para levantar `config-server` antes de los microservicios
- ✅ Mensaje actualizado con puerto del Config Server

#### 📄 Microservicios - POMs

**`config-server/pom.xml`** (NUEVO)
- ✅ Dependencias: `spring-cloud-config-server`, `spring-cloud-starter-config`, `spring-boot-starter-security`, `spring-boot-starter-actuator`
- ✅ Plugin Spring Boot Maven

**`users-service/pom.xml`**
- ✅ Agregado `spring-cloud-starter-config` para importar configuración remota

**`preferences-service/pom.xml`**
- ✅ Agregado `spring-cloud-starter-config` para importar configuración remota

**`intereses-service/pom.xml`**
- ✅ Agregado `spring-cloud-starter-config` para importar configuración remota

**`api-gateway/pom.xml`**
- ✅ Agregado `spring-cloud-starter-config` para importar configuración remota
- ✅ Agregado `spring-boot-starter-oauth2-resource-server` para validar JWT en el gateway

**`eureka-server/pom.xml`**
- ✅ Agregado `spring-cloud-starter-config` para importar configuración remota

#### 📄 Microservicios - Configuración

**`users-service/src/main/resources/application.properties`**
- ❌ **ELIMINADO** (reemplazado por `application.yml`)

**`users-service/src/main/resources/application.yml`** (NUEVO)
- ✅ Configuración mínima: solo `spring.application.name` y `spring.config.import`
- ✅ Importa configuración desde Config Server

**`preferences-service/src/main/resources/application.properties`**
- ❌ **ELIMINADO** (reemplazado por `application.yml`)

**`preferences-service/src/main/resources/application.yml`** (NUEVO)
- ✅ Configuración mínima: solo `spring.application.name` y `spring.config.import`

**`intereses-service/src/main/resources/application.properties`**
- ❌ **ELIMINADO** (reemplazado por `application.yml`)

**`intereses-service/src/main/resources/application.yml`** (NUEVO)
- ✅ Configuración mínima: solo `spring.application.name` y `spring.config.import`

**`api-gateway/src/main/resources/application.properties`**
- ❌ **ELIMINADO** (reemplazado por `application.yml`)

**`api-gateway/src/main/resources/application.yml`** (NUEVO)
- ✅ Configuración mínima: solo `spring.application.name` y `spring.config.import`

**`eureka-server/src/main/resources/application.properties`**
- ❌ **ELIMINADO** (reemplazado por `application.yml`)

**`eureka-server/src/main/resources/application.yml`** (NUEVO)
- ✅ Configuración mínima: solo `spring.application.name` y `spring.config.import`

#### 📄 Configuración Centralizada (config-repo/)

**`config-repo/application.yml`** (NUEVO)
- ✅ Configuración global compartida:
  - Datasource (con password cifrado: `{cipher}049cfafa52aefcc777d3351dfbaefb574f98302e647ef4a07cd3d1f5c2fd49bc`)
  - JPA/Hibernate
  - Eureka client
  - Swagger
  - Actuator
  - Logging

**`config-repo/users-service.yml`** (NUEVO)
- ✅ Configuración específica: puerto 8081, Keycloak OAuth2, Swagger paths

**`config-repo/preferences-service.yml`** (NUEVO)
- ✅ Configuración específica: puerto 8082, Keycloak OAuth2, Swagger paths

**`config-repo/intereses-service.yml`** (NUEVO)
- ✅ Configuración específica: puerto 8084, Keycloak OAuth2, Swagger paths

**`config-repo/api-gateway.yml`** (NUEVO)
- ✅ Configuración específica:
  - Puerto 9000
  - Rutas de Spring Cloud Gateway (users, preferences, intereses, swagger)
  - Keycloak OAuth2 Resource Server
  - Eureka instance

**`config-repo/eureka-server.yml`** (NUEVO)
- ✅ Configuración específica: puerto 8761, Eureka server settings

#### 📄 Config Server

**`config-server/src/main/resources/application.yml`** (NUEVO)
- ✅ Puerto 8888
- ✅ Seguridad básica: `configuser/configpass`
- ✅ Configuración Git: `file:///config-repo`
- ✅ Key de cifrado: `catTinderSuperSecretKey`
- ✅ Actuator endpoints expuestos

**`config-server/src/main/java/com/example/configserver/ConfigServerApplication.java`** (NUEVO)
- ✅ `@SpringBootApplication`
- ✅ `@EnableConfigServer`

**`config-server/src/main/java/com/example/configserver/config/SecurityConfig.java`** (NUEVO)
- ✅ Seguridad HTTP básica para Config Server
- ✅ CSRF deshabilitado para permitir POST a `/crypto/*`
- ✅ Endpoints públicos: `/actuator/**`

**`config-server/src/main/java/com/example/configserver/web/CryptoController.java`** (NUEVO)
- ✅ Endpoint `POST /crypto/encrypt` para cifrar valores
- ✅ Endpoint `POST /crypto/decrypt` para descifrar valores
- ✅ Usa `TextEncryptorLocator` de Spring Cloud Config

**`config-server/Dockerfile`** (NUEVO)
- ✅ Multi-stage build (Maven + JRE)
- ✅ Copia `config-server/pom.xml` para cachear dependencias
- ✅ Expone puerto 8888

#### 📄 Gateway - Seguridad

**`api-gateway/src/main/java/com/example/gateway/config/ApiGatewayConfig.java`**
- ❌ **ELIMINADO** (las rutas ahora están en `config-repo/api-gateway.yml`)

**`api-gateway/src/main/java/com/example/gateway/config/GatewaySecurityConfig.java`** (NUEVO)
- ✅ `SecurityWebFilterChain` reactivo
- ✅ OAuth2 Resource Server con JWT
- ✅ Extracción de roles desde `realm_access.roles` de Keycloak
- ✅ Endpoints públicos: `/actuator/**`, `/swagger/**`, `/v3/api-docs/**`
- ✅ Resto requiere autenticación

#### 📄 Dockerfiles

**`users-service/Dockerfile`**
- ✅ Agregado `COPY config-server/pom.xml` para cachear dependencias
- ✅ Agregado `ENV CONFIG_SERVER_URI=http://config-server:8888`
- ✅ Agregado `ENV CONFIG_SERVER_USER=configuser`
- ✅ Agregado `ENV CONFIG_SERVER_PASSWORD=configpass`

**`preferences-service/Dockerfile`**
- ✅ Agregado `COPY config-server/pom.xml` para cachear dependencias
- ✅ Agregado variables `CONFIG_SERVER_*`

**`intereses-service/Dockerfile`**
- ✅ Agregado `COPY config-server/pom.xml` para cachear dependencias
- ✅ Agregado variables `CONFIG_SERVER_*`

**`api-gateway/Dockerfile`**
- ✅ Agregado `COPY config-server/pom.xml` para cachear dependencias
- ✅ Agregado variables `CONFIG_SERVER_*`

**`eureka-server/Dockerfile`**
- ✅ Agregado `COPY config-server/pom.xml` para cachear dependencias
- ✅ Agregado variables `CONFIG_SERVER_*`

#### 📄 Documentación

**`README.md`** (ACTUALIZADO)
- ✅ Sección completa de Config Server
- ✅ Tabla de evidencias de criterios de evaluación
- ✅ Instrucciones de uso de `/crypto/encrypt`
- ✅ Guía de Authorization Code Flow
- ✅ Troubleshooting

**`KEYCLOAK_SETUP.md`** (ACTUALIZADO)
- ✅ Instrucciones detalladas para crear realm `cattinder`
- ✅ Configuración de roles `ADMIN` y `USER`
- ✅ Creación de clientes confidenciales (microservicios)
- ✅ Creación de cliente público `cattinder-web` con Authorization Code Flow
- ✅ Pasos para obtener token con Authorization Code (Postman)

**`requests.http`** (ACTUALIZADO)
- ✅ Variables actualizadas para apuntar a `http://localhost:9000` (gateway)
- ✅ Ejemplos de peticiones con y sin token

---

## ⚠️ LO QUE FALTA HACER (MANUAL)

### 1. Configurar Keycloak (10-15 minutos)

**Pasos detallados:**

#### 1.1. Levantar servicios
```powershell
docker compose up -d postgres keycloak-db keycloak
```

Espera ~30 segundos a que Keycloak inicie completamente.

#### 1.2. Acceder a Keycloak Admin Console
- URL: http://localhost:8180
- Usuario: `admin`
- Contraseña: `admin`

#### 1.3. Crear Realm "cattinder"
1. Click en el dropdown del realm (arriba izquierda, dice "master")
2. Click en "Create Realm"
3. Nombre: `cattinder`
4. Habilita:
   - ✅ User registration
   - ✅ Remember me
   - ✅ Email as username
   - ✅ Login with email
5. Click "Create"

#### 1.4. Crear Roles
1. Ve a **Realm Settings** → **Roles** (o directamente **Roles** en el menú izquierdo)
2. Click "Create role"
3. Crea:
   - **Role name**: `ADMIN` → Click "Save"
   - **Role name**: `USER` → Click "Save"

#### 1.5. Crear Clientes para Microservicios (Confidenciales)

**Cliente: `users-service`**
1. Ve a **Clients** → Click "Create"
2. **Client ID**: `users-service`
3. **Client Protocol**: `openid-connect`
4. Click "Save"
5. Configura:
   - **Access Type**: `confidential`
   - **Valid Redirect URIs**: `*`
   - **Web Origins**: `*`
   - Ve a pestaña **Service Accounts Roles** → habilita "Client Credentials"
6. Click "Save"
7. Ve a pestaña **Credentials** → copia el **Secret** (lo necesitarás para pruebas)

**Repite para:**
- `preferences-service` (misma configuración)
- `intereses-service` (misma configuración)

#### 1.6. Crear Cliente Público para Authorization Code Flow

**Cliente: `cattinder-web`**
1. Ve a **Clients** → Click "Create"
2. **Client ID**: `cattinder-web`
3. **Client Protocol**: `openid-connect`
4. Click "Save"
5. Configura:
   - **Access Type**: `public` (o `confidential` si prefieres)
   - **Standard Flow Enabled**: ✅ **ON** (esto es Authorization Code)
   - **Direct Access Grants Enabled**: ✅ **ON** (para pruebas con Password Grant)
   - **Valid Redirect URIs**: 
     - `https://oauth.pstmn.io/v1/callback` (para Postman)
     - `http://localhost:3000/*` (si tienes frontend)
   - **Web Origins**: `*`
   - **PKCE Code Challenge Method**: `S256` (recomendado)
6. Click "Save"

#### 1.7. Crear Usuarios de Prueba

**Usuario: `admin`**
1. Ve a **Users** → Click "Add user"
2. **Username**: `admin`
3. **Email**: `admin@cattinder.com`
4. **Email Verified**: ✅ ON
5. Click "Save"
6. Ve a pestaña **Credentials**
7. Click "Set password"
8. **Password**: `admin123`
9. **Temporary**: ⬜ **OFF** (desmarcar)
10. Click "Save"
11. Ve a pestaña **Role Mappings**
12. Click "Assign role"
13. Selecciona `ADMIN`
14. Click "Assign"

**Usuario: `user`**
1. Repite los pasos anteriores con:
   - **Username**: `user`
   - **Email**: `user@cattinder.com`
   - **Password**: `user123`
   - **Rol**: `USER`

#### 1.8. Configurar Mapper de Roles en JWT
1. Ve a **Client Scopes** → Click en `roles`
2. Ve a pestaña **Mappers**
3. Busca "realm roles" → click para editar
4. Verifica:
   - **Token Claim Name**: `realm_access.roles`
   - **Add to ID token**: ✅ ON
   - **Add to access token**: ✅ ON
5. Click "Save"

### 2. Probar el Flujo Completo (5 minutos)

#### 2.1. Levantar todos los servicios
```powershell
docker compose up -d
```

Espera ~1 minuto a que todos los servicios estén listos.

#### 2.2. Verificar Config Server
```powershell
# Debe responder con la configuración
curl -u configuser:configpass http://localhost:8888/intereses-service/default
```

**Evidencia esperada:**
- JSON con `propertySources` mostrando `application.yml` e `intereses-service.yml`
- Propiedad `spring.datasource.password` con valor descifrado (no el `{cipher}...`)

#### 2.3. Obtener Token con Authorization Code Flow (Postman)

1. Abre Postman
2. Crea nueva request: `POST http://localhost:8180/realms/cattinder/protocol/openid-connect/token`
3. Ve a pestaña **Authorization**
4. Selecciona **Type**: `OAuth 2.0`
5. Click "Get New Access Token"
6. Configura:
   - **Grant Type**: `Authorization Code (With PKCE)`
   - **Auth URL**: `http://localhost:8180/realms/cattinder/protocol/openid-connect/auth`
   - **Access Token URL**: `http://localhost:8180/realms/cattinder/protocol/openid-connect/token`
   - **Client ID**: `cattinder-web`
   - **Client Secret**: (dejar vacío si es público)
   - **Scope**: `openid profile email`
   - **Callback URL**: `https://oauth.pstmn.io/v1/callback`
   - **Code Challenge Method**: `SHA256`
7. Click "Get New Access Token"
8. Se abrirá navegador → inicia sesión con `admin/admin123`
9. Postman capturará el token automáticamente
10. Click "Use Token"

#### 2.4. Probar Endpoint del Microservicio vía Gateway

1. En Postman, crea nueva request:
   - **Method**: `POST`
   - **URL**: `http://localhost:9000/intereses/api/v1/interests`
   - **Headers**: 
     - `Content-Type: application/json`
     - `Authorization: Bearer <token_obtenido>`
   - **Body** (raw JSON):
     ```json
     {
       "name": "Gatos"
     }
     ```
2. Click "Send"

**Evidencia esperada:**
- Status: `201 Created` o `200 OK`
- Response body con el interés creado
- Logs del gateway (`docker compose logs -f api-gateway`) muestran la petición
- Logs del microservicio (`docker compose logs -f intereses-service`) muestran la creación

#### 2.5. Verificar Logs

```powershell
# Logs del gateway (debe mostrar la petición con token)
docker compose logs -f api-gateway

# Logs del microservicio (debe mostrar la creación exitosa)
docker compose logs -f intereses-service

# Logs del Config Server (debe mostrar "Fetching config from Git")
docker compose logs -f config-server
```

---

## 📊 CHECKLIST FINAL DE EVIDENCIAS

### Configuración Centralizada

- [x] **Config Server funcional**
  - [ ] Endpoint `/intereses-service/default` responde con configuración
  - [ ] Response incluye `version` (commit hash del repo Git)
  - [ ] Response incluye `propertySources` con `application.yml` e `intereses-service.yml`

- [x] **Configuración externalizada en Git**
  - [ ] Logs del Config Server muestran: `Fetching config from Git`
  - [ ] Logs muestran: `Adding property source: ...config-repo/...yml`
  - [ ] Repo `config-repo/` tiene commits (verificar con `git -C config-repo log`)

- [x] **Config general y específica separada**
  - [ ] `config-repo/application.yml` existe (configuración global)
  - [ ] `config-repo/intereses-service.yml` existe (configuración específica)
  - [ ] Ambas se combinan en la respuesta de `/intereses-service/default`

- [x] **Config Server en docker-compose**
  - [ ] `docker compose ps` muestra `config-server` corriendo
  - [ ] `docker compose ps` muestra todos los servicios corriendo juntos
  - [ ] Logs no muestran errores de conexión al Config Server

- [x] **Propiedad cifrada**
  - [ ] `config-repo/application.yml` contiene `password: {cipher}...`
  - [ ] Endpoint `/intereses-service/default` muestra el password **descifrado** (no el `{cipher}...`)
  - [ ] El microservicio se conecta a la base de datos correctamente (ver logs)

### Keycloak + Authorization Code Flow

- [ ] **Keycloak configurado**
  - [ ] Realm `cattinder` existe (verificar en http://localhost:8180)
  - [ ] Roles `ADMIN` y `USER` existen
  - [ ] Cliente `cattinder-web` existe con Authorization Code Flow habilitado
  - [ ] Usuarios `admin` y `user` existen con roles asignados

- [ ] **Consumo vía Gateway con token válido**
  - [ ] Token obtenido con Authorization Code Flow (Postman)
  - [ ] `POST http://localhost:9000/intereses/api/v1/interests` con token responde `201` o `200`
  - [ ] Logs del gateway muestran la petición autenticada
  - [ ] Logs del microservicio muestran la creación exitosa

---

## 🔧 COMANDOS ÚTILES

### Verificar Config Server
```powershell
# Health check
curl -u configuser:configpass http://localhost:8888/actuator/health

# Obtener configuración de un microservicio
curl -u configuser:configpass http://localhost:8888/intereses-service/default

# Cifrar un valor
curl -u configuser:configpass -H "Content-Type: text/plain" -d "mi_secreto" http://localhost:8888/crypto/encrypt

# Descifrar un valor
curl -u configuser:configpass -H "Content-Type: text/plain" -d "{cipher}..." http://localhost:8888/crypto/decrypt
```

### Verificar servicios
```powershell
# Estado de todos los contenedores
docker compose ps

# Logs del Config Server
docker compose logs -f config-server

# Logs del Gateway
docker compose logs -f api-gateway

# Logs del microservicio de intereses
docker compose logs -f intereses-service

# Logs de todos los servicios
docker compose logs -f
```

### Verificar configuración Git
```powershell
# Estado del repo de configuración
git -C config-repo status

# Historial de commits
git -C config-repo log --oneline

# Ver contenido de un archivo de configuración
cat config-repo/intereses-service.yml
```

### Reiniciar servicios
```powershell
# Reiniciar todo
docker compose restart

# Reiniciar solo Config Server
docker compose restart config-server

# Reconstruir y levantar
docker compose up -d --build
```

---

## 📝 NOTAS IMPORTANTES

1. **Config-repo es un repo Git independiente**: Cualquier cambio en `config-repo/` debe ser commiteado con `git -C config-repo add . && git -C config-repo commit -m "mensaje"` para que el Config Server lo detecte.

2. **Key de cifrado**: La key está hardcodeada en `config-server/src/main/resources/application.yml` como `catTinderSuperSecretKey`. En producción, usa una variable de entorno o un keystore.

3. **Puertos**:
   - Config Server: `8888`
   - Gateway: `9000`
   - Keycloak: `8180` (host) / `8080` (container)
   - Eureka: `8761`
   - Users Service: `8081`
   - Preferences Service: `8082`
   - Intereses Service: `8084`

4. **Credenciales por defecto**:
   - Config Server: `configuser` / `configpass`
   - Keycloak Admin: `admin` / `admin`
   - Postgres: `postgres` / `postgres`

---

## 🆘 SOLUCIÓN DE PROBLEMAS

### Config Server no responde
```powershell
# Verificar que está corriendo
docker compose ps config-server

# Ver logs
docker compose logs config-server

# Verificar que el repo Git está limpio
git -C config-repo status
```

### Microservicio no arranca
```powershell
# Verificar variables de entorno
docker compose exec intereses-service env | grep CONFIG

# Verificar que puede conectarse al Config Server
docker compose exec intereses-service curl -u configuser:configpass http://config-server:8888/actuator/health
```

### Token inválido en Gateway
- Verifica que el token incluye `realm_access.roles` en el payload (usa https://jwt.io)
- Verifica que el usuario tiene roles asignados en Keycloak
- Verifica que el mapper de roles está configurado correctamente

---

## 📚 ARCHIVOS DE REFERENCIA

- `README.md` → Documentación general del proyecto
- `KEYCLOAK_SETUP.md` → Guía detallada de configuración de Keycloak
- `requests.http` → Ejemplos de peticiones HTTP para probar
- `docker-compose.yml` → Definición de todos los servicios
- `config-repo/` → Repositorio de configuración centralizada

---

**Última actualización**: 2025-11-27
**Estado**: ✅ Código completo, ⚠️ Falta configuración manual de Keycloak

