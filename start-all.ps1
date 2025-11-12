Write-Host "Iniciando todos los servicios..." -ForegroundColor Green

Write-Host "Verificando Docker..." -ForegroundColor Yellow
docker ps > $null 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker no está corriendo. Por favor inicia Docker Desktop." -ForegroundColor Red
    exit 1
}

Write-Host "Verificando certificados SSL..." -ForegroundColor Yellow
if (-not (Test-Path "certs\nginx-waf.crt")) {
    Write-Host "Certificados no encontrados. Generando..." -ForegroundColor Yellow
    if (Test-Path "scripts\generate-certs.sh") {
        bash scripts/generate-certs.sh
    } else {
        Write-Host "Script de generación de certificados no encontrado." -ForegroundColor Red
        Write-Host "   Por favor ejecuta: bash scripts/generate-certs.sh" -ForegroundColor Yellow
        exit 1
    }
}

Write-Host "Verificando rutas de proyectos..." -ForegroundColor Yellow
$apiGatewayPath = "C:\UCO\IS2\Workspace-is2-2025\apigateway"
$catalogServicePath = "C:\UCO\IS2\Workspace-is2-2025\catalogservice"
$frontendPath = "C:\Users\santi\Desktop\uco-challenge-fr"

if (-not (Test-Path $apiGatewayPath)) {
    Write-Host "API Gateway no encontrado en: $apiGatewayPath" -ForegroundColor Yellow
    Write-Host "   Por favor actualiza la ruta en docker-compose.yml" -ForegroundColor Yellow
}

if (-not (Test-Path $catalogServicePath)) {
    Write-Host "Catalog Service no encontrado en: $catalogServicePath" -ForegroundColor Yellow
    Write-Host "   Por favor actualiza la ruta en docker-compose.yml" -ForegroundColor Yellow
}

if (-not (Test-Path $frontendPath)) {
    Write-Host "Frontend no encontrado en: $frontendPath" -ForegroundColor Yellow
    Write-Host "   Por favor actualiza la ruta en docker-compose.yml" -ForegroundColor Yellow
}

Write-Host "Construyendo e iniciando servicios..." -ForegroundColor Green
docker-compose up -d --build

if ($LASTEXITCODE -eq 0) {
    Write-Host "Servicios iniciados correctamente!" -ForegroundColor Green
    Write-Host ""
    Write-Host "URLs de acceso:" -ForegroundColor Cyan
    Write-Host "   - Frontend: https://localhost:8443/" -ForegroundColor White
    Write-Host "   - Backend API: https://localhost:8443/uco-challenge/api/v1/" -ForegroundColor White
    Write-Host "   - API Gateway: https://localhost:8443/api/" -ForegroundColor White
    Write-Host "   - Catalog Service: https://localhost:8443/catalog/" -ForegroundColor White
    Write-Host "   - Jaeger UI: http://localhost:16686" -ForegroundColor White
    Write-Host "   - Vault UI: http://localhost:8200" -ForegroundColor White
    Write-Host ""
    Write-Host "Ver estado de servicios:" -ForegroundColor Cyan
    Write-Host "   docker-compose ps" -ForegroundColor White
    Write-Host ""
    Write-Host "Ver logs:" -ForegroundColor Cyan
    Write-Host "   docker-compose logs -f" -ForegroundColor White
    Write-Host ""
    Write-Host "Detener servicios:" -ForegroundColor Cyan
    Write-Host "   docker-compose down" -ForegroundColor White
} else {
    Write-Host "Error al iniciar servicios. Ver logs:" -ForegroundColor Red
    Write-Host "   docker-compose logs" -ForegroundColor Yellow
    exit 1
}

