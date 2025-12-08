# Configuración de Keycloak para CatTinder

## 🚀 Levantar todos los servicios

```bash
docker-compose up --build -d
```

## 📝 Configurar Keycloak

### 1. Acceder a Keycloak Admin Console

- URL: http://localhost:8180
- Usuario: `admin`
- Contraseña: `admin`

### 2. Crear Realm "cattinder"

1. En la esquina superior izquierda, haz clic en el dropdown del realm
2. Selecciona "Create Realm"
3. Nombre del realm: `cattinder`
4. Habilita todas las opciones:
   - ✅ User registration
   - ✅ Remember me
   - ✅ Email as username
   - ✅ Login with email
5. Click "Create"

### 3. Configurar Roles

1. Ve a **Realm Settings** → **Roles**
2. Click en **Create role**
3. Crear los siguientes roles:
   - `ADMIN` - Administradores del sistema
   - `USER` - Usuarios normales

### 4. Crear Cliente (Client) para los microservicios

1. Ve a **Clients** → Click **Create**
2. Configurar:
   - **Client ID**: `users-service`
   - **Client Protocol**: `openid-connect`
3. Click **Save**

4. Configurar el cliente:
   - **Access Type**: `confidential`
   - **Valid Redirect URIs**: `*` (para desarrollo)
   - **Web Origins**: `*`
   - Habilita **Client Credentials** en **Service Accounts Roles**
   
5. Click **Save**

6. Ve a la pestaña **Credentials**
   - Copia el **Secret** (lo necesitarás para configurar client credentials en Postman/curl)

7. Repite el proceso para los demás microservicios:
   - **Client ID**: `preferences-service`
   - **Client ID**: `intereses-service`
   - Misma configuración que `users-service`

### 4.1. Cliente público para Authorization Code Flow

1. Ve a **Clients** → **Create client**
2. Configura:
   - **Client ID**: `cattinder-web`
   - **Client type**: `OpenID Connect`
   - **Authentication**: `Public`
3. En **Capability config** habilita:
   - ✅ `Standard Flow` (Authorization Code)
   - ✅ `Proof Key for Code Exchange (PKCE)`
   - ⬜ `Direct access grants`
4. En **Login settings**:
   - **Root URL**: `https://oauth.pstmn.io`
   - **Valid redirect URIs**: `https://oauth.pstmn.io/v1/callback` (o la URL de tu app web)
   - **Web origins**: `+`
5. Guarda los cambios.

### 5. Crear Usuarios de Prueba

#### Usuario Admin
1. Ve a **Users** → Click **Add user**
2. Configurar:
   - **Username**: `admin`
   - **Email**: `admin@cattinder.com`
   - Habilita **Email Verified**
3. Click **Save**

4. Configurar contraseña:
   - Click en la pestaña **Credentials**
   - Click **Set password**
   - **Password**: `admin123`
   - **Temporary**: ⬜ (deshabilitar)
   - Click **Save**

5. Asignar rol de ADMIN:
   - Click en la pestaña **Role Mappings**
   - Click **Assign role**
   - Selecciona **Filter by role**
   - Busca y selecciona `ADMIN`
   - Click **Assign**

#### Usuario Normal
1. Repite el proceso para un usuario normal:
   - **Username**: `user`
   - **Email**: `user@cattinder.com`
   - **Password**: `user123`
   - Asigna el rol `USER`

### 6. Configurar Client Scopes para incluir roles en JWT

1. Ve a **Client Scopes** → Click en `roles`
2. En la pestaña **Mappers**, busca el mapper llamado `realm roles`
3. Click para editarlo
4. Verifica que **Token Claim Name** esté como: `realm_access.roles`
5. **Add to ID token**: ✅ ON
6. **Add to access token**: ✅ ON
7. Click **Save**

### 7. Probar la autenticación

#### Flujo Authorization Code (recomendado)

1. En Postman (o tu app cliente) selecciona **OAuth 2.0 → Authorization Code (PKCE)**.
2. Completa los datos:
   - **Auth URL**: `http://localhost:8180/realms/cattinder/protocol/openid-connect/auth`
   - **Token URL**: `http://localhost:8180/realms/cattinder/protocol/openid-connect/token`
   - **Client ID**: `cattinder-web`
   - **Scopes**: `openid profile email`
3. Inicia sesión con `admin/admin123` o `user/user123`.
4. Copia el `access_token` resultante y úsalo contra el gateway:

```bash
curl -X POST http://localhost:9000/intereses/api/v1/interests \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"interest-from-web"}'
```

#### Token rápido (Password Grant) para scripts

```bash
curl -X POST http://localhost:8180/realms/cattinder/protocol/openid-connect/token \
  -d "client_id=users-service" \
  -d "client_secret=TU_SECRET_AQUI" \
  -d "username=admin" \
  -d "password=admin123" \
  -d "grant_type=password" \
  -d "scope=openid profile email"
```

Luego:

```bash
TOKEN=$(curl -s -X POST http://localhost:8180/realms/cattinder/protocol/openid-connect/token \
  -d "client_id=users-service" \
  -d "client_secret=TU_SECRET_AQUI" \
  -d "username=admin" \
  -d "password=admin123" \
  -d "grant_type=password" \
  -d "scope=openid profile email" | jq -r '.access_token')

curl -H "Authorization: Bearer $TOKEN" http://localhost:9000/users/api/v1/users
```

## 🎯 Endpoints Protegidos

### Users Service (puerto 8081 / vía gateway `http://localhost:9000/users/**`)

| Endpoint | Método | Roles Requeridos |
|----------|--------|------------------|
| `/api/v1/users` | POST | ADMIN, USER |
| `/api/v1/users` | GET | ADMIN, USER |
| `/api/v1/users/{id}` | GET | ADMIN, USER |
| `/api/v1/users` | PUT | ADMIN |
| `/api/v1/users/{id}` | DELETE | ADMIN |
| `/api/v1/users/{id}/status` | PATCH | ADMIN |

### Preferences Service (puerto 8082 / gateway `/preferences/**`)

| Endpoint | Método | Roles Requeridos |
|----------|--------|------------------|
| `/api/v1/preferences` | POST | ADMIN, USER |
| `/api/v1/preferences/**` | GET | ADMIN, USER |
| `/api/v1/preferences/**` | PUT | ADMIN |
| `/api/v1/preferences/{id}` | DELETE | ADMIN |

## 📌 Endpoints Públicos (sin autenticación)

- `GET http://localhost:9000/<service>/actuator/**` (health via gateway)
- `GET http://localhost:9000/swagger/<service>/swagger-ui/index.html`
- `GET http://localhost:9000/swagger/<service>/v3/api-docs`

## 🔧 Variables de Entorno

Los microservicios están configurados con estas variables:

```env
KEYCLOAK_URL=http://keycloak:8080
KEYCLOAK_REALM=cattinder
KEYCLOAK_RESOURCE=users-service (o preferences-service)
```

## 🐛 Solución de Problemas

### Error: "Issuer URI does not match"

Verifica que el realm esté creado correctamente:
```bash
curl http://localhost:8180/realms/cattinder
```

### Token inválido o expirado

Los tokens JWT de Keycloak tienen una duración de 5 minutos por defecto. Puedes extenderla en:
- **Realm Settings** → **Tokens**
- Ajusta **Access Token Lifespan**

### Error 401 en endpoints protegidos

1. Verifica que el token JWT incluya los roles en `realm_access.roles`
2. Asegúrate de que el usuario tenga el rol asignado
3. Verifica la URL del issuer en `application.properties`

## 📚 Recursos Adicionales

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [JWT.io](https://jwt.io) - Para decodificar tokens JWT

