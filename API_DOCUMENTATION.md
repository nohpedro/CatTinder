# 📚 Documentación de API - CatTinder Microservicios

## Tabla de Contenidos
1. [Users Service](#users-service)
2. [Preferences Service](#preferences-service)
3. [Intereses Service](#intereses-service)
4. [API Gateway](#api-gateway)
5. [Autenticación](#autenticación)

---

## 🔐 Autenticación

Todos los endpoints (excepto los de documentación y health) requieren un token JWT de Keycloak.

### Obtener Token JWT

**Endpoint:** `POST http://localhost:8180/realms/cattinder/protocol/openid-connect/token`

**Headers:**
```
Content-Type: application/x-www-form-urlencoded
```

**Body (form-urlencoded):**
```
grant_type=password
client_id=users-service (o preferences-service, o intereses-service)
username=testuser
password=testpass
```

**Respuesta exitosa:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer"
}
```

**Uso del Token:**
```
Authorization: Bearer {access_token}
```

---

## 1️⃣ Users Service

**Base URL Direct:** `http://localhost:8081`  
**Base URL via Gateway:** `http://localhost:8083/users`  
**Swagger UI:** `http://localhost:8081/swagger-ui.html`

### 📋 Modelo de Datos: User

```json
{
  "id": 1,
  "nombre": "Gatito Pérez",
  "email": "gatito@example.com",
  "edad": 2,
  "biografia": "Soy un gato muy lindo y juguetón",
  "ubicacion": "Buenos Aires, Argentina",
  "activo": true
}
```

### Endpoints

#### 1.1 Crear Usuario
Crea un nuevo usuario en el sistema.

**Endpoint:** `POST /api/v1/users`  
**Rol requerido:** `USER` o `ADMIN`

**Request Body:**
```json
{
  "nombre": "Michi López",
  "email": "michi@example.com",
  "edad": 3,
  "biografia": "Me gusta jugar y dormir",
  "ubicacion": "Madrid, España"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "nombre": "Michi López",
  "email": "michi@example.com",
  "edad": 3,
  "biografia": "Me gusta jugar y dormir",
  "ubicacion": "Madrid, España",
  "activo": true
}
```

**Ejemplo PowerShell:**
```powershell
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

$body = @{
    nombre = "Michi López"
    email = "michi@example.com"
    edad = 3
    biografia = "Me gusta jugar y dormir"
    ubicacion = "Madrid, España"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method POST -Headers $headers -Body $body
```

---

#### 1.2 Obtener Todos los Usuarios
Lista todos los usuarios registrados en el sistema.

**Endpoint:** `GET /api/v1/users`  
**Rol requerido:** `USER` o `ADMIN`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Michi López",
    "email": "michi@example.com",
    "edad": 3,
    "biografia": "Me gusta jugar y dormir",
    "ubicacion": "Madrid, España",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "Luna García",
    "email": "luna@example.com",
    "edad": 5,
    "biografia": "Soy muy elegante",
    "ubicacion": "Barcelona, España",
    "activo": true
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method GET -Headers $headers
```

---

#### 1.3 Obtener Usuario por ID
Obtiene los detalles de un usuario específico.

**Endpoint:** `GET /api/v1/users/{id}`  
**Rol requerido:** `USER` o `ADMIN`

**Path Parameters:**
- `id` (Long): ID del usuario

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Michi López",
  "email": "michi@example.com",
  "edad": 3,
  "biografia": "Me gusta jugar y dormir",
  "ubicacion": "Madrid, España",
  "activo": true
}
```

**Response Error:** `404 Not Found`
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Usuario no encontrado con id: 99",
  "status": 404
}
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/1" -Method GET -Headers $headers
```

---

#### 1.4 Obtener Usuarios Activos
Lista solo los usuarios que están activos en el sistema.

**Endpoint:** `GET /api/v1/users/active`  
**Rol requerido:** `USER` o `ADMIN`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Michi López",
    "email": "michi@example.com",
    "edad": 3,
    "biografia": "Me gusta jugar y dormir",
    "ubicacion": "Madrid, España",
    "activo": true
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/active" -Method GET -Headers $headers
```

---

#### 1.5 Buscar Usuarios por Nombre
Busca usuarios cuyo nombre contenga el texto especificado (case-insensitive).

**Endpoint:** `GET /api/v1/users/search`  
**Rol requerido:** `USER` o `ADMIN`

**Query Parameters:**
- `nombre` (String): Texto a buscar en el nombre

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Michi López",
    "email": "michi@example.com",
    "edad": 3,
    "biografia": "Me gusta jugar y dormir",
    "ubicacion": "Madrid, España",
    "activo": true
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/search?nombre=michi" -Method GET -Headers $headers
```

---

#### 1.6 Contar Usuarios
Retorna el total de usuarios registrados.

**Endpoint:** `GET /api/v1/users/count`  
**Rol requerido:** `USER` o `ADMIN`

**Response:** `200 OK`
```json
15
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/count" -Method GET -Headers $headers
```

---

#### 1.7 Actualizar Usuario
Actualiza la información de un usuario existente.

**Endpoint:** `PUT /api/v1/users/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID del usuario a actualizar

**Request Body:**
```json
{
  "nombre": "Michi López Actualizado",
  "email": "michi.new@example.com",
  "edad": 4,
  "biografia": "Me gusta jugar, dormir y comer",
  "ubicacion": "Valencia, España"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Michi López Actualizado",
  "email": "michi.new@example.com",
  "edad": 4,
  "biografia": "Me gusta jugar, dormir y comer",
  "ubicacion": "Valencia, España",
  "activo": true
}
```

**Ejemplo PowerShell:**
```powershell
$body = @{
    nombre = "Michi López Actualizado"
    email = "michi.new@example.com"
    edad = 4
    biografia = "Me gusta jugar, dormir y comer"
    ubicacion = "Valencia, España"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/1" -Method PUT -Headers $headers -Body $body
```

---

#### 1.8 Eliminar Usuario
Elimina un usuario del sistema (borrado físico).

**Endpoint:** `DELETE /api/v1/users/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID del usuario a eliminar

**Response:** `204 No Content`

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/1" -Method DELETE -Headers $headers
```

---

#### 1.9 Activar/Desactivar Usuario
Cambia el estado activo de un usuario.

**Endpoint:** `PATCH /api/v1/users/{id}/active`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID del usuario

**Query Parameters:**
- `active` (Boolean): `true` para activar, `false` para desactivar

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Michi López",
  "email": "michi@example.com",
  "edad": 3,
  "biografia": "Me gusta jugar y dormir",
  "ubicacion": "Madrid, España",
  "activo": false
}
```

**Ejemplo PowerShell:**
```powershell
# Desactivar usuario
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/1/active?active=false" -Method PATCH -Headers $headers

# Activar usuario
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/1/active?active=true" -Method PATCH -Headers $headers
```

---

## 2️⃣ Preferences Service

**Base URL Direct:** `http://localhost:8082`  
**Base URL via Gateway:** `http://localhost:8083/preferences`  
**Swagger UI:** `http://localhost:8082/swagger-ui.html`

### 📋 Modelo de Datos: Preference

```json
{
  "id": 1,
  "userId": 1,
  "rangoEdadMin": 1,
  "rangoEdadMax": 5,
  "distanciaMaxima": 10,
  "razaPreferida": "Persa"
}
```

### Endpoints

#### 2.1 Crear Preferencia
Crea nuevas preferencias para un usuario.

**Endpoint:** `POST /api/v1/preferences`  
**Rol requerido:** `USER` o `ADMIN`

**Request Body:**
```json
{
  "userId": 1,
  "rangoEdadMin": 1,
  "rangoEdadMax": 5,
  "distanciaMaxima": 10,
  "razaPreferida": "Persa"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "userId": 1,
  "rangoEdadMin": 1,
  "rangoEdadMax": 5,
  "distanciaMaxima": 10,
  "razaPreferida": "Persa"
}
```

**Ejemplo PowerShell:**
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

---

#### 2.2 Obtener Todas las Preferencias
Lista todas las preferencias del sistema.

**Endpoint:** `GET /api/v1/preferences`  
**Rol requerido:** `USER` o `ADMIN`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 1,
    "rangoEdadMin": 1,
    "rangoEdadMax": 5,
    "distanciaMaxima": 10,
    "razaPreferida": "Persa"
  },
  {
    "id": 2,
    "userId": 2,
    "rangoEdadMin": 2,
    "rangoEdadMax": 7,
    "distanciaMaxima": 20,
    "razaPreferida": "Siamés"
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences" -Method GET -Headers $headers
```

---

#### 2.3 Obtener Preferencia por ID
Obtiene una preferencia específica.

**Endpoint:** `GET /api/v1/preferences/{id}`  
**Rol requerido:** `USER` o `ADMIN`

**Path Parameters:**
- `id` (Long): ID de la preferencia

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "rangoEdadMin": 1,
  "rangoEdadMax": 5,
  "distanciaMaxima": 10,
  "razaPreferida": "Persa"
}
```

**Response Error:** `404 Not Found`
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Preferencia no encontrada con id: 99",
  "status": 404
}
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences/1" -Method GET -Headers $headers
```

---

#### 2.4 Obtener Preferencias por Usuario
Obtiene todas las preferencias de un usuario específico.

**Endpoint:** `GET /api/v1/preferences/user/{userId}`  
**Rol requerido:** `USER` o `ADMIN`

**Path Parameters:**
- `userId` (Long): ID del usuario

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 1,
    "rangoEdadMin": 1,
    "rangoEdadMax": 5,
    "distanciaMaxima": 10,
    "razaPreferida": "Persa"
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences/user/1" -Method GET -Headers $headers
```

---

#### 2.5 Actualizar Preferencia
Actualiza una preferencia existente.

**Endpoint:** `PUT /api/v1/preferences/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID de la preferencia

**Request Body:**
```json
{
  "userId": 1,
  "rangoEdadMin": 2,
  "rangoEdadMax": 6,
  "distanciaMaxima": 15,
  "razaPreferida": "Maine Coon"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "rangoEdadMin": 2,
  "rangoEdadMax": 6,
  "distanciaMaxima": 15,
  "razaPreferida": "Maine Coon"
}
```

**Ejemplo PowerShell:**
```powershell
$body = @{
    userId = 1
    rangoEdadMin = 2
    rangoEdadMax = 6
    distanciaMaxima = 15
    razaPreferida = "Maine Coon"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences/1" -Method PUT -Headers $headers -Body $body
```

---

#### 2.6 Eliminar Preferencia
Elimina una preferencia del sistema.

**Endpoint:** `DELETE /api/v1/preferences/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID de la preferencia

**Response:** `204 No Content`

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences/1" -Method DELETE -Headers $headers
```

---

## 3️⃣ Intereses Service (NUEVO)

**Base URL Direct:** `http://localhost:8084`  
**Base URL via Gateway:** `http://localhost:8083/intereses`  
**Swagger UI:** `http://localhost:8084/swagger-ui.html`

### 📋 Modelo de Datos: Interest

```json
{
  "id": 1,
  "nombre": "Jugar con pelotas",
  "descripcion": "Me encanta perseguir pelotas de lana",
  "userId": 1
}
```

### Endpoints

#### 3.1 Crear Interés
Crea un nuevo interés para un usuario.

**Endpoint:** `POST /api/v1/interests`  
**Rol requerido:** `USER` o `ADMIN`

**Request Body:**
```json
{
  "nombre": "Jugar con pelotas",
  "descripcion": "Me encanta perseguir pelotas de lana",
  "userId": 1
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "nombre": "Jugar con pelotas",
  "descripcion": "Me encanta perseguir pelotas de lana",
  "userId": 1
}
```

**Validaciones:**
- `nombre`: Obligatorio, no puede estar vacío

**Response Error (Validación):** `400 Bad Request`
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Error de validación",
  "status": 400,
  "errors": {
    "nombre": "El nombre del interés es obligatorio"
  }
}
```

**Ejemplo PowerShell:**
```powershell
$body = @{
    nombre = "Jugar con pelotas"
    descripcion = "Me encanta perseguir pelotas de lana"
    userId = 1
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method POST -Headers $headers -Body $body
```

---

#### 3.2 Obtener Todos los Intereses
Lista todos los intereses del sistema.

**Endpoint:** `GET /api/v1/interests`  
**Rol requerido:** `USER` o `ADMIN`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Jugar con pelotas",
    "descripcion": "Me encanta perseguir pelotas de lana",
    "userId": 1
  },
  {
    "id": 2,
    "nombre": "Dormir al sol",
    "descripcion": "Me gusta dormir bajo el sol en la ventana",
    "userId": 1
  },
  {
    "id": 3,
    "nombre": "Cazar ratones de juguete",
    "descripcion": "Los ratones de juguete son mi pasión",
    "userId": 2
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method GET -Headers $headers
```

---

#### 3.3 Obtener Interés por ID
Obtiene un interés específico por su ID.

**Endpoint:** `GET /api/v1/interests/{id}`  
**Rol requerido:** `USER` o `ADMIN`

**Path Parameters:**
- `id` (Long): ID del interés

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Jugar con pelotas",
  "descripcion": "Me encanta perseguir pelotas de lana",
  "userId": 1
}
```

**Response Error:** `404 Not Found`
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Interés no encontrado con id: 99",
  "status": 404
}
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/1" -Method GET -Headers $headers
```

---

#### 3.4 Obtener Intereses por Usuario
Obtiene todos los intereses de un usuario específico.

**Endpoint:** `GET /api/v1/interests/user/{userId}`  
**Rol requerido:** `USER` o `ADMIN`

**Path Parameters:**
- `userId` (Long): ID del usuario

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Jugar con pelotas",
    "descripcion": "Me encanta perseguir pelotas de lana",
    "userId": 1
  },
  {
    "id": 2,
    "nombre": "Dormir al sol",
    "descripcion": "Me gusta dormir bajo el sol en la ventana",
    "userId": 1
  }
]
```

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/user/1" -Method GET -Headers $headers
```

---

#### 3.5 Buscar Intereses por Nombre
Busca intereses cuyo nombre contenga el texto especificado (case-insensitive).

**Endpoint:** `GET /api/v1/interests/search`  
**Rol requerido:** `USER` o `ADMIN`

**Query Parameters:**
- `nombre` (String): Texto a buscar en el nombre del interés

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Jugar con pelotas",
    "descripcion": "Me encanta perseguir pelotas de lana",
    "userId": 1
  }
]
```

**Ejemplo PowerShell:**
```powershell
# Buscar intereses que contengan "jugar"
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/search?nombre=jugar" -Method GET -Headers $headers

# Buscar intereses que contengan "dormir"
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/search?nombre=dormir" -Method GET -Headers $headers
```

---

#### 3.6 Actualizar Interés
Actualiza un interés existente.

**Endpoint:** `PUT /api/v1/interests/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID del interés

**Request Body:**
```json
{
  "nombre": "Jugar con pelotas grandes",
  "descripcion": "Me encantan especialmente las pelotas grandes de lana",
  "userId": 1
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Jugar con pelotas grandes",
  "descripcion": "Me encantan especialmente las pelotas grandes de lana",
  "userId": 1
}
```

**Ejemplo PowerShell:**
```powershell
$body = @{
    nombre = "Jugar con pelotas grandes"
    descripcion = "Me encantan especialmente las pelotas grandes de lana"
    userId = 1
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/1" -Method PUT -Headers $headers -Body $body
```

---

#### 3.7 Eliminar Interés
Elimina un interés del sistema.

**Endpoint:** `DELETE /api/v1/interests/{id}`  
**Rol requerido:** `ADMIN` (solo administradores)

**Path Parameters:**
- `id` (Long): ID del interés

**Response:** `204 No Content`

**Ejemplo PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/1" -Method DELETE -Headers $headers
```

---

## 4️⃣ API Gateway

**Base URL:** `http://localhost:8083`

### Rutas Configuradas

El API Gateway redirige automáticamente las peticiones a los microservicios correspondientes:

| Ruta en Gateway | Microservicio Destino | Puerto Directo |
|----------------|----------------------|----------------|
| `/users/**` | users-service | 8081 |
| `/preferences/**` | preferences-service | 8082 |
| `/intereses/**` | intereses-service | 8084 |
| `/swagger/users/**` | users-service (swagger) | 8081 |
| `/swagger/preferences/**` | preferences-service (swagger) | 8082 |
| `/swagger/intereses/**` | intereses-service (swagger) | 8084 |

### Ejemplo de Reescritura de Rutas

**Petición al Gateway:**
```
GET http://localhost:8083/intereses/api/v1/interests
```

**Se traduce a:**
```
GET http://intereses-service:8084/api/v1/interests
```

### Health Check

**Endpoint:** `GET /actuator/health`  
**No requiere autenticación**

**Response:** `200 OK`
```json
{
  "status": "UP"
}
```

---

## 📊 Resumen de Permisos por Rol

### Role: USER
Puede realizar:
- ✅ Crear recursos (POST)
- ✅ Leer recursos (GET)
- ❌ Actualizar recursos (PUT)
- ❌ Eliminar recursos (DELETE)
- ❌ Cambiar estados (PATCH)

### Role: ADMIN
Puede realizar:
- ✅ Crear recursos (POST)
- ✅ Leer recursos (GET)
- ✅ Actualizar recursos (PUT)
- ✅ Eliminar recursos (DELETE)
- ✅ Cambiar estados (PATCH)

---

## 🔍 Códigos de Estado HTTP

| Código | Significado | Cuándo se usa |
|--------|-------------|---------------|
| 200 | OK | Operación GET, PUT, PATCH exitosa |
| 201 | Created | Recurso creado exitosamente (POST) |
| 204 | No Content | Recurso eliminado exitosamente (DELETE) |
| 400 | Bad Request | Error de validación en los datos enviados |
| 401 | Unauthorized | Token JWT inválido o ausente |
| 403 | Forbidden | Usuario no tiene permisos suficientes (rol incorrecto) |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error interno del servidor |

---

## 🧪 Ejemplos de Flujos Completos

### Flujo 1: Crear Usuario y sus Preferencias e Intereses

```powershell
# 1. Obtener token
$body = @{
    grant_type = "password"
    client_id = "users-service"
    username = "testuser"
    password = "testpass"
}
$response = Invoke-RestMethod -Uri "http://localhost:8180/realms/cattinder/protocol/openid-connect/token" -Method POST -ContentType "application/x-www-form-urlencoded" -Body $body
$token = $response.access_token

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# 2. Crear usuario
$userBody = @{
    nombre = "Felix Gato"
    email = "felix@example.com"
    edad = 3
    biografia = "Soy un gato aventurero"
    ubicacion = "Lima, Perú"
} | ConvertTo-Json

$user = Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method POST -Headers $headers -Body $userBody

Write-Host "Usuario creado con ID: $($user.id)"

# 3. Crear preferencias para el usuario
$prefBody = @{
    userId = $user.id
    rangoEdadMin = 2
    rangoEdadMax = 5
    distanciaMaxima = 15
    razaPreferida = "Angora"
} | ConvertTo-Json

$pref = Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences" -Method POST -Headers $headers -Body $prefBody

Write-Host "Preferencia creada con ID: $($pref.id)"

# 4. Crear intereses para el usuario
$interest1 = @{
    nombre = "Cazar insectos"
    descripcion = "Me fascina cazar moscas y mariposas"
    userId = $user.id
} | ConvertTo-Json

$interest1Result = Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method POST -Headers $headers -Body $interest1

$interest2 = @{
    nombre = "Escalar árboles"
    descripcion = "Soy un experto escalador"
    userId = $user.id
} | ConvertTo-Json

$interest2Result = Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests" -Method POST -Headers $headers -Body $interest2

Write-Host "Intereses creados con IDs: $($interest1Result.id), $($interest2Result.id)"

# 5. Obtener perfil completo
Write-Host "`n=== PERFIL COMPLETO ==="
Write-Host "`nUsuario:"
Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users/$($user.id)" -Method GET -Headers $headers | ConvertTo-Json

Write-Host "`nPreferencias:"
Invoke-RestMethod -Uri "http://localhost:8083/preferences/api/v1/preferences/user/$($user.id)" -Method GET -Headers $headers | ConvertTo-Json

Write-Host "`nIntereses:"
Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/user/$($user.id)" -Method GET -Headers $headers | ConvertTo-Json
```

---

### Flujo 2: Búsqueda de Usuarios con Intereses Similares

```powershell
# Buscar usuarios que les guste "jugar"
$usuarios = Invoke-RestMethod -Uri "http://localhost:8083/users/api/v1/users" -Method GET -Headers $headers

foreach ($usuario in $usuarios) {
    $intereses = Invoke-RestMethod -Uri "http://localhost:8083/intereses/api/v1/interests/user/$($usuario.id)" -Method GET -Headers $headers
    
    $tieneJugar = $intereses | Where-Object { $_.nombre -like "*jugar*" }
    
    if ($tieneJugar) {
        Write-Host "Usuario: $($usuario.nombre) - Intereses relacionados con jugar:"
        $tieneJugar | ForEach-Object { Write-Host "  - $($_.nombre): $($_.descripcion)" }
    }
}
```

---

## 🛡️ Manejo de Errores

Todos los microservicios devuelven errores en formato JSON consistente:

```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Descripción del error",
  "status": 400,
  "errors": {
    "campo1": "Mensaje de error del campo 1",
    "campo2": "Mensaje de error del campo 2"
  }
}
```

### Ejemplos de Errores Comunes

#### Error de Validación (400)
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Error de validación",
  "status": 400,
  "errors": {
    "nombre": "El nombre del interés es obligatorio",
    "email": "El email debe ser válido"
  }
}
```

#### Error de Autenticación (401)
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Credenciales inválidas",
  "status": 401
}
```

#### Error de Permisos (403)
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Acceso denegado",
  "status": 403
}
```

#### Recurso No Encontrado (404)
```json
{
  "timestamp": "2025-10-30T15:30:00",
  "message": "Usuario no encontrado con id: 99",
  "status": 404
}
```

---

## 📝 Notas Adicionales

### Paginación
Actualmente los endpoints no implementan paginación. Se recomienda implementarla en futuras versiones para listas grandes.

### Rate Limiting
No hay límites de peticiones configurados. Se recomienda implementar rate limiting en producción.

### CORS
Los servicios están configurados para aceptar peticiones desde cualquier origen en desarrollo. Ajustar en producción.

### Logs
Todos los servicios registran las operaciones importantes. Ver logs con:
```bash
docker logs users-service
docker logs preferences-service
docker logs intereses-service
```

---

## 🔗 Enlaces Útiles

- **Eureka Dashboard:** http://localhost:8761
- **Keycloak Admin:** http://localhost:8180
- **API Gateway:** http://localhost:8083
- **Users Swagger:** http://localhost:8081/swagger-ui.html
- **Preferences Swagger:** http://localhost:8082/swagger-ui.html
- **Intereses Swagger:** http://localhost:8084/swagger-ui.html

---

**Última actualización:** 30 de Octubre, 2025  
**Versión de la API:** 1.0  
**Autor:** CatTinder Development Team

