Write-Host "=== DIAGNÓSTICO CONFIG-SERVER ===" -ForegroundColor Cyan

Write-Host "`n1. Deteniendo contenedor..." -ForegroundColor Yellow
docker compose down config-server 2>&1 | Out-Null

Write-Host "`n2. Verificando config-repo..." -ForegroundColor Yellow
if (Test-Path "config-repo") {
    Write-Host "✓ config-repo existe" -ForegroundColor Green
    $files = Get-ChildItem "config-repo" -File
    Write-Host "  Archivos: $($files.Count)" -ForegroundColor Green
    $files | ForEach-Object { Write-Host "    - $($_.Name)" }
    
    Set-Location config-repo
    $gitStatus = git status 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Es un repo Git válido" -ForegroundColor Green
        $commits = git log --oneline -1 2>&1
        Write-Host "  Último commit: $commits" -ForegroundColor Green
    } else {
        Write-Host "✗ NO es un repo Git válido" -ForegroundColor Red
        Write-Host "  Error: $gitStatus" -ForegroundColor Red
    }
    Set-Location ..
} else {
    Write-Host "✗ config-repo NO existe" -ForegroundColor Red
}

Write-Host "`n3. Construyendo imagen..." -ForegroundColor Yellow
$buildOutput = docker compose build config-server 2>&1
$buildSuccess = $LASTEXITCODE -eq 0
if ($buildSuccess) {
    Write-Host "✓ Build exitoso" -ForegroundColor Green
} else {
    Write-Host "✗ Build falló" -ForegroundColor Red
    $buildOutput | Select-Object -Last 20
    exit 1
}

Write-Host "`n4. Levantando servicio..." -ForegroundColor Yellow
docker compose up -d config-server 2>&1 | Out-Null
Start-Sleep -Seconds 5

Write-Host "`n5. Verificando estado..." -ForegroundColor Yellow
$containerStatus = docker ps -a --filter "name=config-server" --format "{{.Status}}" 2>&1
Write-Host "Estado: $containerStatus" -ForegroundColor $(if ($containerStatus -match "Up") { "Green" } else { "Red" })

Write-Host "`n6. Logs del contenedor:" -ForegroundColor Yellow
$logs = docker compose logs config-server --tail 50 2>&1
if ($logs) {
    $logs
} else {
    Write-Host "  (sin logs)" -ForegroundColor Yellow
}

Write-Host "`n7. Verificando si el contenedor está corriendo..." -ForegroundColor Yellow
$running = docker ps --filter "name=config-server" --format "{{.Names}}"
if ($running) {
    Write-Host "✓ Contenedor corriendo: $running" -ForegroundColor Green
    
    Write-Host "`n8. Probando endpoint..." -ForegroundColor Yellow
    Start-Sleep -Seconds 3
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8888/actuator/health" `
            -Method Get `
            -Headers @{Authorization=("Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("configuser:configpass")))} `
            -TimeoutSec 5 `
            -ErrorAction Stop
        Write-Host "✓ Endpoint responde: $($response.StatusCode)" -ForegroundColor Green
        Write-Host "  Contenido: $($response.Content)" -ForegroundColor Green
    } catch {
        Write-Host "✗ Endpoint no responde" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "✗ Contenedor NO está corriendo" -ForegroundColor Red
    Write-Host "`nRevisando logs de error..." -ForegroundColor Yellow
    docker compose logs config-server --tail 100 2>&1 | Select-String -Pattern "ERROR|Error|error|Exception|Failed|failed" | Select-Object -Last 10
}

Write-Host "`n=== FIN DIAGNÓSTICO ===" -ForegroundColor Cyan

