# Script para probar Keycloak y obtener tokens

Write-Host "=== Test de Keycloak Token ===" -ForegroundColor Cyan
Write-Host ""

# Verificar que Keycloak esté corriendo
Write-Host "1. Verificando Keycloak..." -ForegroundColor Yellow
try {
    $realm = Invoke-RestMethod -Uri "http://localhost:8180/realms/cattinder" -Method Get
    Write-Host "✓ Keycloak está corriendo" -ForegroundColor Green
    Write-Host "  Realm: $($realm.realm)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Error: Keycloak no está disponible en http://localhost:8180" -ForegroundColor Red
    Write-Host "  Asegúrate de que Keycloak esté corriendo: docker-compose up -d keycloak" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Obtener token usando Password Grant (para pruebas)
Write-Host "2. Obteniendo token de Keycloak..." -ForegroundColor Yellow
Write-Host "   Usuario: admin / Contraseña: admin123" -ForegroundColor Gray

$tokenUrl = "http://localhost:8180/realms/cattinder/protocol/openid-connect/token"
$body = @{
    client_id = "cattinder-web"
    username = "admin"
    password = "admin123"
    grant_type = "password"
    scope = "openid profile email"
}

try {
    $tokenResponse = Invoke-RestMethod -Uri $tokenUrl -Method Post -Body $body -ContentType "application/x-www-form-urlencoded"
    
    $accessToken = $tokenResponse.access_token
    Write-Host "✓ Token obtenido exitosamente" -ForegroundColor Green
    Write-Host ""
    Write-Host "Access Token (primeros 50 caracteres):" -ForegroundColor Cyan
    Write-Host $accessToken.Substring(0, [Math]::Min(50, $accessToken.Length)) -ForegroundColor Gray
    Write-Host ""
    
    # Decodificar el token para ver su contenido
    Write-Host "3. Decodificando token JWT..." -ForegroundColor Yellow
    $tokenParts = $accessToken.Split('.')
    if ($tokenParts.Length -eq 3) {
        # Decodificar el payload (segunda parte)
        $payload = $tokenParts[1]
        # Agregar padding si es necesario
        while ($payload.Length % 4) {
            $payload += "="
        }
        $payloadBytes = [System.Convert]::FromBase64String($payload)
        $payloadJson = [System.Text.Encoding]::UTF8.GetString($payloadBytes) | ConvertFrom-Json
        
        Write-Host "✓ Token decodificado" -ForegroundColor Green
        Write-Host ""
        Write-Host "Información del token:" -ForegroundColor Cyan
        Write-Host "  Usuario: $($payloadJson.preferred_username)" -ForegroundColor Gray
        Write-Host "  Email: $($payloadJson.email)" -ForegroundColor Gray
        Write-Host "  Roles:" -ForegroundColor Gray
        if ($payloadJson.realm_access.roles) {
            foreach ($role in $payloadJson.realm_access.roles) {
                Write-Host "    - $role" -ForegroundColor Gray
            }
        }
        Write-Host ""
    }
    
    # Probar el token con un endpoint protegido
    Write-Host "4. Probando token con endpoint protegido..." -ForegroundColor Yellow
    
    $headers = @{
        "Authorization" = "Bearer $accessToken"
        "Content-Type" = "application/json"
    }
    
    # Probar con users-service
    Write-Host "   Probando: http://localhost:9000/users/api/v1/users" -ForegroundColor Gray
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:9000/users/api/v1/users" -Method Get -Headers $headers -ErrorAction Stop
        Write-Host "✓ Token funciona correctamente!" -ForegroundColor Green
        Write-Host "  Respuesta recibida del servicio" -ForegroundColor Gray
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "✗ Error al usar el token: $statusCode" -ForegroundColor Red
        Write-Host "  Mensaje: $($_.Exception.Message)" -ForegroundColor Yellow
        
        if ($statusCode -eq 401) {
            Write-Host ""
            Write-Host "Posibles causas:" -ForegroundColor Yellow
            Write-Host "  1. El servicio no está corriendo" -ForegroundColor Gray
            Write-Host "  2. El servicio no puede conectarse a Keycloak" -ForegroundColor Gray
            Write-Host "  3. El token no tiene los roles necesarios" -ForegroundColor Gray
            Write-Host "  4. La URL de Keycloak en el servicio es incorrecta" -ForegroundColor Gray
        }
    }
    
    Write-Host ""
    Write-Host "=== Token completo (para usar en Postman/curl) ===" -ForegroundColor Cyan
    Write-Host $accessToken -ForegroundColor White
    Write-Host ""
    
} catch {
    Write-Host "✗ Error al obtener token: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Verifica:" -ForegroundColor Yellow
    Write-Host "  1. Que el realm 'cattinder' esté creado en Keycloak" -ForegroundColor Gray
    Write-Host "  2. Que el cliente 'cattinder-web' esté configurado" -ForegroundColor Gray
    Write-Host "  3. Que el usuario 'admin' exista con contraseña 'admin123'" -ForegroundColor Gray
    Write-Host "  4. Que el usuario tenga el rol 'ADMIN' asignado" -ForegroundColor Gray
    exit 1
}


