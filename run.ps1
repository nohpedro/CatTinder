Param(
    [switch]$Rebuild = $false,
    [switch]$FollowLogs = $true
)

Write-Host "==> Iniciando stack CatTinder" -ForegroundColor Cyan
Set-Location $PSScriptRoot

if ($Rebuild) {
  Write-Host "==> Build de imágenes (no-cache)" -ForegroundColor Yellow
  docker compose build --no-cache
}

Write-Host "==> Levantando dependencias (Postgres, Keycloak, Config Server, Eureka)" -ForegroundColor Cyan
docker compose up -d postgres keycloak-db keycloak config-server eureka-server

Write-Host "==> Levantando microservicios y gateway" -ForegroundColor Cyan
docker compose up -d users-service preferences-service intereses-service api-gateway

Write-Host "==> Estado de contenedores" -ForegroundColor Cyan
docker compose ps

if ($FollowLogs) {
  Write-Host "==> Siguiendo logs del gateway (Ctrl+C para salir)" -ForegroundColor Cyan
  docker compose logs -f api-gateway
}

Write-Host "==> Listo. Gateway en http://localhost:9000 | Config Server en http://localhost:8888 | Keycloak en http://localhost:8180 | Eureka en http://localhost:8761" -ForegroundColor Green


