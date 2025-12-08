Write-Host "=== Diagnóstico del Config Server ===" -ForegroundColor Cyan
Write-Host ""

# Verificar estado del contenedor
Write-Host "1. Estado del contenedor:" -ForegroundColor Yellow
docker ps -a --filter "name=config-server" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
Write-Host ""

# Ver logs del contenedor
Write-Host "2. Últimos 50 líneas de logs:" -ForegroundColor Yellow
docker logs config-server --tail 50
Write-Host ""

# Verificar si la imagen existe
Write-Host "3. Imágenes relacionadas:" -ForegroundColor Yellow
docker images | Select-String "config-server"
Write-Host ""

# Intentar ejecutar el contenedor y ver el error
Write-Host "4. Intentando ejecutar el contenedor para ver el error:" -ForegroundColor Yellow
docker run --rm --name test-config-server cattinder-config-server 2>&1 | Select-Object -First 20
Write-Host ""

# Verificar el JAR dentro de la imagen
Write-Host "5. Verificando el JAR en la imagen:" -ForegroundColor Yellow
$containerId = docker create cattinder-config-server
if ($containerId) {
    docker cp "${containerId}:/app/app.jar" ./test-app.jar
    if (Test-Path "./test-app.jar") {
        Write-Host "JAR copiado. Verificando manifest..." -ForegroundColor Green
        jar xf test-app.jar META-INF/MANIFEST.MF
        if (Test-Path "META-INF/MANIFEST.MF") {
            Get-Content META-INF/MANIFEST.MF | Select-String "Main-Class"
            Remove-Item -Recurse -Force META-INF
        }
        Remove-Item test-app.jar
    }
    docker rm $containerId
}
Write-Host ""

Write-Host "=== Fin del diagnóstico ===" -ForegroundColor Cyan

