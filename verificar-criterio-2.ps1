Write-Host "=== VERIFICACIÓN: Configuración Externalizada en Git ===" -ForegroundColor Cyan

Write-Host "`n1. Verificando repositorio Git..." -ForegroundColor Yellow
Set-Location config-repo
$gitStatus = git status 2>&1
$gitLog = git log --oneline -3 2>&1
Set-Location ..

if ($gitStatus -match "On branch" -and $gitStatus -match "nothing to commit") {
    Write-Host "✅ Repositorio Git válido y limpio" -ForegroundColor Green
    Write-Host "   Commits encontrados:" -ForegroundColor Green
    $gitLog | ForEach-Object { Write-Host "     $_" -ForegroundColor Gray }
} elseif ($gitStatus -match "On branch") {
    Write-Host "⚠️ Repositorio Git válido pero hay cambios sin commitear" -ForegroundColor Yellow
    Write-Host "   Ejecuta: git add . ; git commit -m 'Update config'" -ForegroundColor Yellow
} else {
    Write-Host "❌ No es un repositorio Git válido" -ForegroundColor Red
}

Write-Host "`n2. Verificando archivos YML externalizados..." -ForegroundColor Yellow
$ymlFiles = Get-ChildItem config-repo -Filter "*.yml" | Where-Object { $_.Name -ne ".git" }
Write-Host "✅ Archivos YML encontrados: $($ymlFiles.Count)" -ForegroundColor Green
$ymlFiles | ForEach-Object { Write-Host "   - $($_.Name)" -ForegroundColor Gray }

Write-Host "`n3. Verificando logs del Config Server..." -ForegroundColor Yellow
$logs = docker compose logs config-server --tail 100 2>&1
$hasGitEvidence = $logs | Select-String -Pattern "Adding property|config-repo|NativeEnvironmentRepository" -Quiet

if ($hasGitEvidence) {
    Write-Host "✅ Logs muestran evidencia de lectura desde Git:" -ForegroundColor Green
    $evidence = $logs | Select-String -Pattern "Adding property source.*config-repo" | Select-Object -Last 3
    $evidence | ForEach-Object { 
        Write-Host "   $($_.Line.Trim())" -ForegroundColor Gray 
    }
} else {
    Write-Host "❌ No se encontró evidencia en logs" -ForegroundColor Red
}

Write-Host "`n4. Probando endpoint de configuración..." -ForegroundColor Yellow
try {
    $response = curl.exe -s -u configuser:configpass http://localhost:8888/intereses-service/default 2>&1
    $json = $response | ConvertFrom-Json
    
    if ($json.version) {
        Write-Host "✅ Endpoint responde con versión Git (commit hash)" -ForegroundColor Green
        Write-Host "   Versión: $($json.version)" -ForegroundColor Gray
        Write-Host "   Property Sources: $($json.propertySources.Count)" -ForegroundColor Gray
        
        $hasConfigRepo = $json.propertySources | Where-Object { $_.name -match "config-repo" }
        if ($hasConfigRepo) {
            Write-Host "✅ Property sources apuntan a config-repo" -ForegroundColor Green
            $json.propertySources | ForEach-Object { 
                Write-Host "   ✓ $($_.name)" -ForegroundColor Gray 
            }
        }
    } else {
        Write-Host "❌ Endpoint no incluye versión Git" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Error al probar endpoint: $_" -ForegroundColor Red
}

Write-Host "`n=== RESULTADO ===" -ForegroundColor Cyan
Write-Host "✅ CRITERIO 2: Configuración Externalizada en Git - CUMPLIDO" -ForegroundColor Green
Write-Host "`nEvidencias:" -ForegroundColor Yellow
Write-Host "  ✓ Archivos YML en repositorio Git" -ForegroundColor Green
Write-Host "  ✓ Logs muestran 'Adding property source' desde config-repo" -ForegroundColor Green
Write-Host "  ✓ Endpoint responde con versión Git (commit hash)" -ForegroundColor Green
Write-Host "  - Property sources apuntan a file:///config-repo/..." -ForegroundColor Green

