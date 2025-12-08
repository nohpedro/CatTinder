Write-Host "=== Probando Config Server ===" -ForegroundColor Cyan

Write-Host "`n1. Deteniendo contenedor existente..." -ForegroundColor Yellow
docker compose down config-server 2>&1 | Out-Null

Write-Host "`n2. Construyendo imagen..." -ForegroundColor Yellow
$buildResult = docker compose build config-server 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR en build:" -ForegroundColor Red
    $buildResult
    exit 1
}
Write-Host "Build exitoso!" -ForegroundColor Green

Write-Host "`n3. Levantando servicio..." -ForegroundColor Yellow
docker compose up -d config-server 2>&1 | Out-Null
Start-Sleep -Seconds 10

Write-Host "`n4. Verificando estado del contenedor..." -ForegroundColor Yellow
$status = docker ps --filter "name=config-server" --format "{{.Status}}"
if ($status) {
    Write-Host "Contenedor: $status" -ForegroundColor Green
} else {
    Write-Host "ERROR: Contenedor no está corriendo" -ForegroundColor Red
    docker compose logs config-server --tail 30
    exit 1
}

Write-Host "`n5. Verificando logs..." -ForegroundColor Yellow
$logs = docker compose logs config-server --tail 20
$logs

Write-Host "`n6. Probando endpoint de health..." -ForegroundColor Yellow
$health = curl.exe -s -u configuser:configpass http://localhost:8888/actuator/health 2>&1
if ($health -match "UP" -or $health -match "status") {
    Write-Host "Health check exitoso!" -ForegroundColor Green
    $health
} else {
    Write-Host "ERROR en health check:" -ForegroundColor Red
    $health
}

Write-Host "`n7. Probando endpoint de configuración..." -ForegroundColor Yellow
$config = curl.exe -s -u configuser:configpass http://localhost:8888/intereses-service/default 2>&1
if ($config -match "propertySources" -or $config -match "name") {
    Write-Host "Config endpoint exitoso!" -ForegroundColor Green
} else {
    Write-Host "ERROR en config endpoint:" -ForegroundColor Red
    $config
}

Write-Host "`n=== Prueba completada ===" -ForegroundColor Cyan

