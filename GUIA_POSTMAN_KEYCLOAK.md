# 🔐 Guía de Postman con Keycloak para CatTinder

## 📋 Requisitos Previos

- Keycloak corriendo en `http://localhost:8180`
- Realm `cattinder` creado
- Usuario `admin` con contraseña `admin123` y rol `ADMIN`
- Cliente `cattinder-web` configurado con `Direct access grants` habilitado
- Servicios corriendo (users-service, preferences-service, etc.)

---

## 🚀 Método 1: OAuth 2.0 Authorization Code (Recomendado)

### Paso 1: Configurar OAuth 2.0 en Postman

1. Abre Postman
2. Crea una nueva **Collection** llamada "CatTinder API"
3. Ve a la pestaña **Authorization** de la collection
4. Selecciona **Type: OAuth 2.0**
5. Configura los siguientes campos:

```
Grant Type: Authorization Code (With PKCE)
Callback URL: https://oauth.pstmn.io/v1/callback
Auth URL: http://localhost:8180/realms/cattinder/protocol/openid-connect/auth
Access Token URL: http://localhost:8180/realms/cattinder/protocol/openid-connect/token
Client ID: cattinder-web
Client Secret: (dejar vacío si es público)
Scope: openid profile email
State: (dejar vacío)
Client Authentication: Send as Basic Auth header (o Send client credentials in body)
```

6. Click en **Get New Access Token**
7. Se abrirá una ventana del navegador para autenticarte
8. Ingresa:
   - Usuario: `admin`
   - Contraseña: `admin123`
9. Click en **Use Token**
10. El token se guardará automáticamente en la collection

### Paso 2: Crear Requests

1. Crea un nuevo request en la collection
2. El request heredará automáticamente la autenticación OAuth 2.0
3. Configura el request:

```
Method: GET
URL: http://localhost:9000/users/api/v1/users
```

4. Click en **Send**
5. El token se enviará automáticamente en el header `Authorization: Bearer <token>`

---

## 🔑 Método 2: Password Grant (Más Simple para Pruebas)

### Paso 1: Obtener Token Manualmente

1. Crea un nuevo request en Postman
2. Configura:

```
Method: POST
URL: http://localhost:8180/realms/cattinder/protocol/openid-connect/token
```

3. Ve a la pestaña **Body**
4. Selecciona **x-www-form-urlencoded**
5. Agrega los siguientes campos:

| Key | Value |
|-----|-------|
| client_id | cattinder-web |
| username | admin |
| password | admin123 |
| grant_type | password |
| scope | openid profile email |

6. Click en **Send**
7. Copia el `access_token` de la respuesta JSON

### Paso 2: Usar el Token en Requests

1. Crea un nuevo request para probar un endpoint
2. Configura:

```
Method: GET
URL: http://localhost:9000/users/api/v1/users
```

3. Ve a la pestaña **Authorization**
4. Selecciona **Type: Bearer Token**
5. Pega el `access_token` que copiaste
6. Click en **Send**

---

## 🎯 Método 3: Variable de Entorno (Más Eficiente)

### Paso 1: Crear Variables de Entorno

1. En Postman, click en el icono de **Environments** (ojo) en la esquina superior derecha
2. Click en **+** para crear un nuevo environment
3. Nómbralo "CatTinder Local"
4. Agrega las siguientes variables:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `keycloak_url` | http://localhost:8180 | http://localhost:8180 |
| `keycloak_realm` | cattinder | cattinder |
| `keycloak_client` | cattinder-web | cattinder-web |
| `gateway_url` | http://localhost:9000 | http://localhost:9000 |
| `access_token` | (dejar vacío) | (se llenará automáticamente) |

5. Guarda el environment
6. Selecciona este environment en el dropdown

### Paso 2: Crear Request para Obtener Token

1. Crea un nuevo request llamado "Get Token"
2. Configura:

```
Method: POST
URL: {{keycloak_url}}/realms/{{keycloak_realm}}/protocol/openid-connect/token
```

3. En **Body** → **x-www-form-urlencoded**:

| Key | Value |
|-----|-------|
| client_id | {{keycloak_client}} |
| username | admin |
| password | admin123 |
| grant_type | password |
| scope | openid profile email |

4. Ve a la pestaña **Tests** y agrega este script:

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.access_token);
    console.log("Token guardado en variable de entorno");
}
```

5. Click en **Send**
6. El token se guardará automáticamente en la variable `access_token`

### Paso 3: Usar el Token en Otros Requests

1. Crea un nuevo request para cualquier endpoint
2. Configura:

```
Method: GET
URL: {{gateway_url}}/users/api/v1/users
```

3. En **Authorization** → **Bearer Token**:
   - Token: `{{access_token}}`

4. Click en **Send**

---

## 📝 Ejemplos de Requests

### 1. Obtener Todos los Usuarios

```
Method: GET
URL: {{gateway_url}}/users/api/v1/users
Authorization: Bearer {{access_token}}
```

### 2. Crear un Usuario

```
Method: POST
URL: {{gateway_url}}/users/api/v1/users
Authorization: Bearer {{access_token}}
Content-Type: application/json

Body (raw JSON):
{
  "username": "nuevo_usuario",
  "email": "nuevo@example.com",
  "password": "password123",
  "fullName": "Nuevo Usuario"
}
```

### 3. Obtener Preferencias

```
Method: GET
URL: {{gateway_url}}/preferences/api/v1/preferences
Authorization: Bearer {{access_token}}
```

### 4. Crear Preferencias

```
Method: POST
URL: {{gateway_url}}/preferences/api/v1/preferences
Authorization: Bearer {{access_token}}
Content-Type: application/json

Body (raw JSON):
{
  "userId": 1,
  "ageRange": {
    "min": 18,
    "max": 30
  },
  "maxDistance": 10
}
```

### 5. Obtener Intereses

```
Method: GET
URL: {{gateway_url}}/intereses/api/v1/interests
Authorization: Bearer {{access_token}}
```

---

## 🔍 Verificar el Token

Para ver qué contiene tu token JWT:

1. Copia el `access_token`
2. Ve a https://jwt.io
3. Pega el token en el campo "Encoded"
4. Verás el contenido decodificado:
   - `preferred_username`: El usuario
   - `realm_access.roles`: Los roles del usuario
   - `exp`: Fecha de expiración

---

## ⚠️ Solución de Problemas

### Error 401 Unauthorized

**Causas posibles:**
1. Token expirado → Obtén un nuevo token
2. Token no válido → Verifica que el token sea correcto
3. Usuario sin roles → Asigna roles al usuario en Keycloak

**Solución:**
- Obtén un nuevo token
- Verifica que el usuario tenga el rol `ADMIN` o `USER` asignado

### Error 403 Forbidden

**Causa:** El usuario no tiene los permisos necesarios para el endpoint

**Solución:**
- Verifica que el usuario tenga el rol correcto
- Algunos endpoints requieren `ADMIN`, otros aceptan `ADMIN` o `USER`

### Error "Issuer URI does not match"

**Causa:** El servicio no puede conectarse a Keycloak

**Solución:**
- Verifica que los servicios tengan las variables `KEYCLOAK_URL` y `KEYCLOAK_REALM`
- Verifica que Keycloak esté corriendo: `docker ps | grep keycloak`
- Verifica los logs del servicio: `docker logs users-service`

### Token no se renueva automáticamente

**Solución:**
- Usa el Método 3 (Variables de Entorno) con el script de Tests
- O configura OAuth 2.0 con Auto Refresh Token habilitado

---

## 📚 Endpoints Disponibles

### Users Service (`/users/**`)

| Método | Endpoint | Roles Requeridos |
|--------|----------|------------------|
| GET | `/api/v1/users` | ADMIN, USER |
| POST | `/api/v1/users` | ADMIN, USER |
| GET | `/api/v1/users/{id}` | ADMIN, USER |
| PUT | `/api/v1/users/{id}` | ADMIN |
| DELETE | `/api/v1/users/{id}` | ADMIN |

### Preferences Service (`/preferences/**`)

| Método | Endpoint | Roles Requeridos |
|--------|----------|------------------|
| GET | `/api/v1/preferences` | ADMIN, USER |
| POST | `/api/v1/preferences` | ADMIN, USER |
| PUT | `/api/v1/preferences/{id}` | ADMIN |
| DELETE | `/api/v1/preferences/{id}` | ADMIN |

### Intereses Service (`/intereses/**`)

| Método | Endpoint | Roles Requeridos |
|--------|----------|------------------|
| GET | `/api/v1/interests` | ADMIN, USER |
| POST | `/api/v1/interests` | ADMIN, USER |
| PUT | `/api/v1/interests/{id}` | ADMIN |
| DELETE | `/api/v1/interests/{id}` | ADMIN |

---

## 🎉 ¡Listo!

Ahora puedes probar todos los endpoints protegidos de CatTinder usando Postman con autenticación Keycloak.


