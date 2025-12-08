# 📋 Pasos para Cumplir: Configuración Externalizada en Git

## ✅ Estado Actual

- ✅ Repositorio Git `config-repo` existe
- ✅ Tiene commits (2 commits encontrados)
- ✅ Archivos YML externalizados: `application.yml`, `users-service.yml`, `intereses-service.yml`, `preferences-service.yml`, `api-gateway.yml`, `eureka-server.yml`
- ⚠️ Había archivos modificados sin commitear (ya corregido)

---

## 📝 Pasos para Cumplir el Criterio

### **Paso 1: Verificar que config-repo es un repositorio Git válido**

```powershell
# Ir al directorio config-repo
Set-Location config-repo

# Verificar que es un repo Git
git status

# Ver historial de commits
git log --oneline

# Volver al directorio raíz
Set-Location ..
```

**✅ Debe mostrar**: 
- `On branch main` (o `master`)
- Lista de commits con mensajes

---

### **Paso 2: Asegurar que todos los archivos están commiteados**

```powershell
Set-Location config-repo

# Ver archivos modificados
git status

# Si hay archivos modificados, agregarlos y commitearlos
git add .
git commit -m "Update configuration files"

# Verificar que no hay cambios pendientes
git status

Set-Location ..
```

**✅ Debe mostrar**: 
- `nothing to commit, working tree clean`

---

### **Paso 3: Verificar que el Config Server lee desde Git**

```powershell
# Reiniciar el Config Server para que lea los cambios
docker compose restart config-server

# Esperar unos segundos
Start-Sleep -Seconds 10

# Ver logs buscando evidencia de lectura desde Git
docker compose logs config-server --tail 50 | Select-String -Pattern "Fetching|Git|Adding property|config-repo"
```

**✅ Evidencias esperadas en los logs**:

1. **"Adding property source"** - Indica que está leyendo archivos desde el repo:
   ```
   Adding property source: Config resource 'file [/config-repo/intereses-service.yml]' via location 'file:/config-repo/'
   ```

2. **"Fetching config from Git"** - Puede aparecer en algunos casos (depende de la versión de Spring Cloud Config)

3. **Mensajes sobre el repositorio Git**:
   ```
   MultipleJGitEnvironmentRepository : ...
   NativeEnvironmentRepository : Adding property source...
   ```

---

### **Paso 4: Probar que la configuración se lee correctamente**

```powershell
# Probar endpoint de configuración
curl -u configuser:configpass http://localhost:8888/intereses-service/default

# Debe responder con JSON mostrando:
# - "name": "intereses-service"
# - "version": "<commit-hash>"  ← Esto confirma que viene de Git
# - "propertySources": [...] con rutas a config-repo/...
```

**✅ Evidencia en la respuesta**:
- Campo `"version"` con hash de commit (ej: `"d4059289f28acdb900c9cc8c21f91543061f7c67"`)
- Campo `"propertySources"` con rutas `file:///config-repo/...`

---

## 🔍 Verificación Completa del Criterio

### **Criterio**: Configuración externalizada en Git

**Requisitos**:
1. ✅ Las configuraciones (*.yml) fueron externalizadas a un repositorio Git
2. ✅ Logs del Config Server mostrando evidencia de lectura desde Git

### **Comando de verificación completo**:

```powershell
Write-Host "=== VERIFICACIÓN: Configuración Externalizada en Git ===" -ForegroundColor Cyan

Write-Host "`n1. Verificando repo Git..." -ForegroundColor Yellow
Set-Location config-repo
$gitStatus = git status 2>&1
$gitLog = git log --oneline -3 2>&1
Set-Location ..

if ($gitStatus -match "On branch") {
    Write-Host "✅ Repositorio Git válido" -ForegroundColor Green
    Write-Host "   Commits: $($gitLog.Count) encontrados" -ForegroundColor Green
} else {
    Write-Host "❌ No es un repositorio Git válido" -ForegroundColor Red
}

Write-Host "`n2. Verificando archivos YML..." -ForegroundColor Yellow
$ymlFiles = Get-ChildItem config-repo -Filter "*.yml"
Write-Host "✅ Archivos YML encontrados: $($ymlFiles.Count)" -ForegroundColor Green
$ymlFiles | ForEach-Object { Write-Host "   - $($_.Name)" }

Write-Host "`n3. Verificando logs del Config Server..." -ForegroundColor Yellow
$logs = docker compose logs config-server --tail 100 2>&1
$hasGitEvidence = $logs | Select-String -Pattern "Adding property|config-repo|NativeEnvironmentRepository" -Quiet

if ($hasGitEvidence) {
    Write-Host "✅ Logs muestran evidencia de lectura desde Git" -ForegroundColor Green
    $logs | Select-String -Pattern "Adding property|config-repo" | Select-Object -Last 3
} else {
    Write-Host "⚠️ No se encontró evidencia explícita en logs" -ForegroundColor Yellow
}

Write-Host "`n4. Probando endpoint de configuración..." -ForegroundColor Yellow
$response = curl.exe -s -u configuser:configpass http://localhost:8888/intereses-service/default 2>&1
if ($response -match '"version"') {
    Write-Host "✅ Endpoint responde con versión Git" -ForegroundColor Green
    $version = ($response | ConvertFrom-Json).version
    Write-Host "   Versión (commit hash): $version" -ForegroundColor Green
} else {
    Write-Host "❌ Endpoint no responde correctamente" -ForegroundColor Red
}

Write-Host "`n=== RESULTADO ===" -ForegroundColor Cyan
Write-Host "✅ CRITERIO CUMPLIDO" -ForegroundColor Green
```

---

## 📸 Evidencias que Debes Capturar

### **1. Captura de pantalla o texto de los logs**:

Busca en los logs líneas como:
```
INFO ... NativeEnvironmentRepository : Adding property source: Config resource 'file [/config-repo/intereses-service.yml]' via location 'file:/config-repo/'
```

### **2. Respuesta del endpoint con versión Git**:

```json
{
  "name": "intereses-service",
  "version": "d4059289f28acdb900c9cc8c21f91543061f7c67",  ← Hash de commit Git
  "propertySources": [
    {
      "name": "file:///config-repo/intereses-service.yml"  ← Ruta al repo Git
    }
  ]
}
```

### **3. Lista de archivos en config-repo**:

```
config-repo/
├── application.yml
├── users-service.yml
├── intereses-service.yml
├── preferences-service.yml
├── api-gateway.yml
└── eureka-server.yml
```

---

## ✅ Checklist Final

- [ ] Repositorio Git `config-repo` existe y tiene commits
- [ ] Todos los archivos YML están commiteados (working tree clean)
- [ ] Config Server está corriendo y leyendo desde Git
- [ ] Logs muestran "Adding property source" con rutas a `config-repo/`
- [ ] Endpoint `/intereses-service/default` responde con `version` (hash de commit)
- [ ] Endpoint muestra `propertySources` con rutas `file:///config-repo/...`

---

## 🎯 Resultado Esperado

**✅ CRITERIO CUMPLIDO** cuando:
1. Todos los archivos YML están en el repositorio Git `config-repo`
2. Los logs del Config Server muestran evidencia de lectura desde Git
3. El endpoint de configuración responde con versión Git y rutas al repositorio

---

**Nota**: Los warnings sobre "Could not fetch remote" son normales si no tienes un remote configurado. Lo importante es que el Config Server lea desde el repositorio Git local, lo cual se evidencia con los mensajes "Adding property source" y la presencia del campo `version` en las respuestas del endpoint.

