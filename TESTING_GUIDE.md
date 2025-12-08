# Guía de Pruebas - CatTinder Microservicios

## 🚀 Estado de los Servicios

Todos los microservicios están corriendo correctamente:

| Servicio | Puerto | URL | Estado |
|----------|---------|-----|--------|
| **Eureka Server** | 8761 | http://localhost:8761 | ✅ Running |
| **API Gateway** | 8083 | http://localhost:8083 | ✅ Running |
| **Users Service** | 8081 | http://localhost:8081 | ✅ Running |
| **Preferences Service** | 8082 | http://localhost:8082 | ✅ Running |
| **Intereses Service** | 8084 | http://localhost:8084 | ✅ Running |
| **Keycloak** | 8180 | http://localhost:8180 | ✅ Running |
| **PostgreSQL** | 5432 | localhost:5432 | ✅ Running |

---

## 🔐 Configurar Keycloak (IMPORTANTE - Hazlo primero)

### 1. Acceder a Keycloak Admin Console
```
URL: http://localhost:8180
Usuario: admin
Contraseña: admin
```

### 2. Crear Realm
1. Click en "Create Realm"
2. Nombre: `cattinder`
3. Click "Create"

### 3. Crear Roles
1. Ve a "Realm roles" → "Create role"
2. Crea estos roles:
   - `ADMIN`
   - `USER`

### 4. Crear Client para cada Microservicio
Para cada servicio (users-service, preferences-service, intereses-service):

1. Ve a "Clients" → "Create client"
2. **Client ID**: `users-service` (o el nombre correspondiente)
3. **Client authentication**: ON
4. **Authorization**: OFF
5. **Valid redirect URIs**: `*`
6. **Web origins**: `*`
7. Click "Save"

Repite para:
- `preferences-service`
- `intereses-service`

### 5. Crear Usuarios de Prueba
1. Ve a "Users" → "Add user"
2. **Username**: `testuser`
3. **Email**: `testuser@example.com`
4. **Email verified**: ON
5. Click "Create"
6. Ve a tab "Credentials":
   - Set password: `testpass`
   - Temporary: OFF
7. Ve a tab "Role mapping":
   - Assign role: `USER`

Repite para crear un admin:
- Username: `adminuser`
- Password: `adminpass`
- Role: `ADMIN`

---

## 🔑 Obtener Token JWT

### Usando curl (Windows PowerShell):
```powershell
$body = @{
    grant_type = "password"
    client_id = "users-service"
    username = "testuser"
    password = "testpass"
}

$response = Invoke-RestMethod -Uri "http://localhost:8180/realms/cattinder/protocol/openid-connect/token" -Method POST -ContentType "application/x-www-form-urlencoded" -Body $body

$token = $response.access_token
Write-Host "Token obtenido: $token"
```

### Usando curl (Linux/Mac):
```bash
curl -X POST http://localhost:8180/realms/cattinder/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=users-service" \
  -d "username=testuser" \
  -d "password=testpass"
```

---

## 📡 Probar Endpoints (a través del API Gateway)

### 1. Verificar Eureka Dashboard
```
URL: http://localhost:8761
```
Deberías ver registrados:
- USERS-SERVICE
- PREFERENCES-SERVICE
- INTERESES-SERVICE
- API-GATEWAY

### 2. Probar Users Service

#### Crear Usuario (requiere token)
```powershell
# PowerShell
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

$body = @{
    nombre = "Gatito Pérez"
    email = "gatito@example.com"
    edad = 2
    biografia = "Soy un gato muy lindo"
    ubicacion = "Buenos Aires"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method POST -Headers $headers -Body $body
```

#### Obtener Todos los Usuarios
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method GET -Headers $headers
```

### 3. Probar Preferences Service

#### Crear Preferencia
```powershell
$body = @{
    userId = 1
    rangoEdadMin = 1
    rangoEdadMax = 5
    distanciaMaxima = 10
    razaPreferida = "Persa"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences" -Method POST -Headers $headers -Body $body
```

#### Obtener Todas las Preferencias
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences" -Method GET -Headers $headers
```

### 4. Probar Intereses Service (NUEVO)

#### Crear Interés
```powershell
$body = @{
    nombre = "Jugar con pelotas"
    descripcion = "Me encanta perseguir pelotas de lana"
    userId = 1
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method POST -Headers $headers -Body $body
```

#### Obtener Todos los Intereses
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method GET -Headers $headers
```

#### Obtener Intereses por Usuario
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/user/1" -Method GET -Headers $headers
```

#### Buscar Intereses por Nombre
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/search?nombre=jugar" -Method GET -Headers $headers
```

---

## 📚 Documentación Swagger

Cada microservicio tiene su propia documentación Swagger:

- **Users Service**: http://localhost:8081/swagger-ui.html
- **Preferences Service**: http://localhost:8082/swagger-ui.html
- **Intereses Service**: http://localhost:8084/swagger-ui.html

**A través del API Gateway**:
- Users: http://localhost:8083/swagger/users/swagger-ui.html
- Preferences: http://localhost:8083/swagger/preferences/swagger-ui.html
- Intereses: http://localhost:8083/swagger/intereses/swagger-ui.html

---

## 🐳 Comandos Docker Útiles

### Ver todos los contenedores
```bash
docker-compose ps
```

### Ver logs de un servicio específico
```bash
docker logs intereses-service --tail 50 -f
```

### Reiniciar un servicio específico
```bash
docker-compose restart intereses-service
```

### Reconstruir y reiniciar todo
```bash
docker-compose down -v
docker-compose up -d --build
```

### Detener todos los servicios
```bash
docker-compose down
```

### Ver logs de todos los servicios
```bash
docker-compose logs -f
```

---

## 🔒 Seguridad - Roles y Permisos

### Intereses Service:
- **POST /api/v1/interests**: Requiere rol `ADMIN` o `USER`
- **GET /api/v1/interests**: Requiere rol `ADMIN` o `USER`
- **GET /api/v1/interests/{id}**: Requiere rol `ADMIN` o `USER`
- **GET /api/v1/interests/user/{userId}**: Requiere rol `ADMIN` o `USER`
- **GET /api/v1/interests/search**: Requiere rol `ADMIN` o `USER`
- **PUT /api/v1/interests/{id}**: Requiere rol `ADMIN` (solo)
- **DELETE /api/v1/interests/{id}**: Requiere rol `ADMIN` (solo)

### Users Service & Preferences Service:
Tienen configuraciones similares de seguridad basada en roles.

---

## ✅ Verificación Completa

### 1. Verificar Base de Datos
```bash
# Conectarse a PostgreSQL
docker exec -it postgres psql -U postgres -d cattinder

# Verificar tablas
\dt

# Deberías ver: users, preferences, interests
```

### 2. Verificar Registro en Eureka
- Ve a http://localhost:8761
- Verifica que todos los servicios estén registrados

### 3. Probar Health Endpoints
```powershell
# Eureka
Invoke-RestMethod -Uri "http://localhost:8761/actuator/health"

# API Gateway
Invoke-RestMethod -Uri "http://localhost:8083/actuator/health"

# Users Service
Invoke-RestMethod -Uri "http://localhost:8081/actuator/health"

# Preferences Service
Invoke-RestMethod -Uri "http://localhost:8082/actuator/health"

# Intereses Service
Invoke-RestMethod -Uri "http://localhost:8084/actuator/health"
```

---

## 🎉 ¡Todo Listo!

Tu arquitectura de microservicios está completamente operativa con:
- ✅ 3 Microservicios (Users, Preferences, Intereses)
- ✅ API Gateway con routing configurado
- ✅ Eureka Server para service discovery
- ✅ Keycloak para autenticación JWT
- ✅ PostgreSQL como base de datos compartida
- ✅ Docker Compose orchestrando todo
- ✅ Seguridad basada en roles
- ✅ Documentación Swagger disponible

**Nota**: Recuerda configurar Keycloak antes de probar los endpoints protegidos.

