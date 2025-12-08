# 📮 Guía de Importación y Uso - Postman Collection

## 📥 Cómo Importar la Colección en Postman

### Paso 1: Importar la Colección

1. Abre **Postman**
2. Click en el botón **"Import"** (esquina superior izquierda)
3. Arrastra y suelta el archivo `CatTinder_Postman_Collection.json` 
   - O click en **"Choose Files"** y selecciona el archivo
4. Click en **"Import"**
5. ✅ Verás la colección **"CatTinder - Microservicios API"** en el panel izquierdo

### Paso 2: Importar el Environment (Opcional pero Recomendado)

1. Click en el botón **"Import"**
2. Arrastra y suelta el archivo `CatTinder_Postman_Environment.json`
3. Click en **"Import"**
4. En la esquina superior derecha, selecciona **"CatTinder - Development"** del dropdown de environments
5. ✅ Ahora todas las variables de entorno están configuradas

---

## 🚀 Uso de la Colección

### Estructura de la Colección

La colección está organizada en 5 carpetas principales:

```
CatTinder - Microservicios API
├── 0. Authentication (Keycloak)      ← Autenticación JWT
├── 1. Users Service                  ← 10 endpoints
├── 2. Preferences Service            ← 6 endpoints
├── 3. Intereses Service              ← 7 endpoints
├── 4. Health Checks                  ← 5 endpoints de salud
└── 5. Complete Workflows             ← Flujos completos de prueba
```

---

## 🔐 PASO IMPORTANTE: Obtener Token JWT

**Antes de probar cualquier endpoint, debes obtener un token JWT.**

### Opción 1: Usuario con Rol USER

1. Expande la carpeta **"0. Authentication (Keycloak)"**
2. Click en **"Get Token - USER Role"**
3. Click en **"Send"**
4. ✅ El token se guardará automáticamente en la variable `{{access_token}}`

**Credenciales:**
- Username: `testuser`
- Password: `testpass`
- Client: `users-service`

### Opción 2: Usuario con Rol ADMIN

1. Click en **"Get Token - ADMIN Role"**
2. Click en **"Send"**
3. ✅ El token se guardará automáticamente en la variable `{{access_token}}`

**Credenciales:**
- Username: `adminuser`
- Password: `adminpass`
- Client: `users-service`

### Ver el Token Guardado

1. Click en el icono del ojo 👁️ (esquina superior derecha)
2. Busca la variable `access_token`
3. Verás el token JWT guardado

---

## 📋 Flujo de Prueba Recomendado

### 1️⃣ Verificar que los servicios están corriendo

**Carpeta:** `4. Health Checks`

Ejecuta estos endpoints (no requieren token):
1. ✅ Gateway Health
2. ✅ Users Service Health
3. ✅ Preferences Service Health
4. ✅ Intereses Service Health
5. ✅ Eureka Dashboard

**Resultado esperado:** Todos deben retornar `200 OK` con `"status": "UP"`

---

### 2️⃣ Obtener Token JWT

**Carpeta:** `0. Authentication (Keycloak)`

1. Ejecuta **"Get Token - USER Role"**
2. ✅ Verifica en la consola que se guardó el token

---

### 3️⃣ Probar Users Service

**Carpeta:** `1. Users Service`

Ejecuta en orden:

1. **Create User** → Guarda el `user_id` automáticamente
2. **Get All Users** → Verifica que tu usuario aparece
3. **Get User by ID** → Usa el `{{user_id}}` guardado
4. **Search Users by Name** → Busca "gatito"
5. **Count Users** → Verifica el total

**Con rol ADMIN** (obtén token ADMIN primero):
6. **Update User** → Modifica los datos
7. **Deactivate User** → Desactiva el usuario
8. **Activate User** → Reactiva el usuario
9. **Delete User** → Elimina el usuario

---

### 4️⃣ Probar Preferences Service

**Carpeta:** `2. Preferences Service`

1. **Create Preference** → Usa el `{{user_id}}` de un usuario existente
2. **Get All Preferences**
3. **Get Preference by ID**
4. **Get Preferences by User** → Filtra por usuario

**Con rol ADMIN:**
5. **Update Preference**
6. **Delete Preference**

---

### 5️⃣ Probar Intereses Service (NUEVO)

**Carpeta:** `3. Intereses Service`

1. **Create Interest** → Guarda el `interest_id` automáticamente
2. **Get All Interests**
3. **Get Interest by ID**
4. **Get Interests by User** → Filtra por usuario
5. **Search Interests by Name** → Busca "jugar"

**Con rol ADMIN:**
6. **Update Interest**
7. **Delete Interest**

---

### 6️⃣ Ejecutar Flujo Completo

**Carpeta:** `5. Complete Workflows → Create Complete User Profile`

Ejecuta los pasos en orden:
1. **Step 1 - Create User** → Crea usuario "Luna García"
2. **Step 2 - Create Preferences** → Crea preferencias para Luna
3. **Step 3 - Create Interest 1** → "Dormir al sol"
4. **Step 4 - Create Interest 2** → "Cazar insectos"
5. **Step 5 - Get Complete Profile** → Obtiene todos los datos

✅ Ahora tienes un perfil completo con usuario, preferencias e intereses

---

## 🔄 Variables Automáticas

La colección usa **scripts automáticos** que guardan IDs después de crear recursos:

| Variable | Se guarda en | Se usa en |
|----------|--------------|-----------|
| `{{access_token}}` | Get Token | Todos los endpoints protegidos |
| `{{user_id}}` | Create User | Get/Update/Delete User, Create Preference/Interest |
| `{{preference_id}}` | Create Preference | Get/Update/Delete Preference |
| `{{interest_id}}` | Create Interest | Get/Update/Delete Interest |
| `{{workflow_user_id}}` | Workflow Step 1 | Workflow Steps 2-5 |

**No necesitas copiar y pegar IDs manualmente** ✨

---

## 📝 Modificar Datos de Prueba

Puedes modificar los datos en el **Body** de cada request:

### Ejemplo: Crear Usuario

```json
{
    "nombre": "Tu Gato Aquí",
    "email": "tugato@example.com",
    "edad": 5,
    "biografia": "Descripción personalizada",
    "ubicacion": "Tu ciudad"
}
```

### Ejemplo: Crear Interés

```json
{
    "nombre": "Tu interés",
    "descripcion": "Descripción del interés",
    "userId": {{user_id}}
}
```

---

## 🛡️ Autenticación Automática

La colección está configurada con **Bearer Token Authentication** a nivel de colección:

- ✅ Todos los endpoints usan automáticamente `{{access_token}}`
- ✅ No necesitas agregar headers manualmente
- ✅ Solo ejecuta "Get Token" y los demás endpoints funcionarán

**Excepción:** Los endpoints de Health Checks no requieren token.

---

## ⚠️ Solución de Problemas

### Error 401 Unauthorized

**Problema:** El token expiró o no existe.

**Solución:**
1. Ve a `0. Authentication (Keycloak)`
2. Ejecuta nuevamente **"Get Token - USER Role"** o **"Get Token - ADMIN Role"**
3. Reintenta el endpoint

---

### Error 403 Forbidden

**Problema:** Tu usuario no tiene permisos suficientes.

**Solución:**
1. Si el endpoint requiere rol ADMIN, obtén el token ADMIN:
   - Ejecuta **"Get Token - ADMIN Role"**
2. Verifica en la documentación qué rol necesita el endpoint

**Endpoints que requieren ADMIN:**
- PUT (actualizar)
- DELETE (eliminar)
- PATCH (cambiar estado)

---

### Error 404 Not Found

**Problema:** El recurso con ese ID no existe.

**Solución:**
1. Verifica que el ID es correcto
2. Ejecuta "Get All" para ver los IDs disponibles
3. O crea un nuevo recurso antes de intentar obtenerlo

---

### Error 400 Bad Request

**Problema:** Datos de entrada inválidos.

**Solución:**
1. Revisa el **Body** del request
2. Verifica que los campos obligatorios estén presentes
3. Verifica el formato de los datos (números, strings, etc.)

**Campos obligatorios:**
- **User:** nombre, email
- **Preference:** userId
- **Interest:** nombre

---

### Error Connection Refused

**Problema:** Los servicios no están corriendo.

**Solución:**
```bash
# Verifica que Docker esté corriendo
docker ps

# Si no hay contenedores, levanta los servicios
docker-compose up -d

# Espera 30 segundos y verifica de nuevo
docker ps
```

---

## 🎯 Tips y Trucos

### 1. Ejecutar múltiples requests en secuencia

Postman permite ejecutar toda una carpeta:
1. Click derecho en una carpeta (ej: "1. Users Service")
2. Click en **"Run folder"**
3. ✅ Se ejecutarán todos los endpoints en orden

### 2. Ver la consola de Postman

Para debug:
1. Click en **"Console"** (parte inferior)
2. Verás todos los requests/responses con detalles

### 3. Guardar variables manualmente

Si necesitas guardar un valor:
```javascript
// En la pestaña "Tests" de un request
pm.collectionVariables.set("mi_variable", valor);
```

### 4. Exportar resultados

Para compartir resultados:
1. Ejecuta "Run folder"
2. Click en **"Export Results"**
3. Comparte el archivo JSON

### 5. Duplicar requests

Para crear variaciones:
1. Click derecho en un request
2. **"Duplicate"**
3. Modifica el duplicado

---

## 📊 Resumen de Endpoints por Servicio

### Users Service (10 endpoints)
- ✅ 6 GET (lectura)
- ✅ 1 POST (crear)
- ✅ 1 PUT (actualizar)
- ✅ 1 PATCH (activar/desactivar)
- ✅ 1 DELETE (eliminar)

### Preferences Service (6 endpoints)
- ✅ 3 GET (lectura)
- ✅ 1 POST (crear)
- ✅ 1 PUT (actualizar)
- ✅ 1 DELETE (eliminar)

### Intereses Service (7 endpoints)
- ✅ 4 GET (lectura)
- ✅ 1 POST (crear)
- ✅ 1 PUT (actualizar)
- ✅ 1 DELETE (eliminar)

**Total: 23 endpoints** listos para probar ✨

---

## 📚 Documentación Adicional

Para más detalles sobre cada endpoint:
- 📄 **API_DOCUMENTATION.md** - Documentación completa de la API
- 📄 **TESTING_GUIDE.md** - Guía de pruebas con PowerShell/curl
- 📄 **KEYCLOAK_SETUP.md** - Configuración de Keycloak

---

## 🎉 ¡Listo para Probar!

Tu colección de Postman incluye:
- ✅ **23+ endpoints** listos para usar
- ✅ **Autenticación automática** con JWT
- ✅ **Variables auto-guardadas** (no copiar/pegar IDs)
- ✅ **Scripts de test** que validan respuestas
- ✅ **Flujos completos** pre-configurados
- ✅ **Documentación** en cada request

**Siguiente paso:** 
1. ✅ Importa la colección
2. ✅ Obtén un token JWT
3. ✅ ¡Empieza a probar!

---

**Última actualización:** 30 de Octubre, 2025  
**Versión:** 1.0  
**Autor:** CatTinder Development Team

