# Script simplificado para ejecutar migraciones SQL
# Ejecuta el SQL directamente desde el archivo usando docker exec

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Ejecutando migración de tokens de confirmación" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que el contenedor está corriendo
Write-Host "Verificando contenedor de PostgreSQL..." -ForegroundColor Yellow
$containerRunning = docker ps --filter "name=postgres-db" --format "{{.Names}}"

if (-not $containerRunning) {
    Write-Host "ERROR: El contenedor 'postgres-db' no está en ejecución." -ForegroundColor Red
    Write-Host "Ejecuta: docker-compose up -d db" -ForegroundColor Yellow
    Write-Host "O espera a que el contenedor esté listo y vuelve a intentar." -ForegroundColor Yellow
    exit 1
}

Write-Host "Contenedor encontrado: $containerRunning" -ForegroundColor Green
Write-Host ""

# Verificar que el archivo existe
# Obtener el directorio del script actual
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$scriptPath = Join-Path $scriptDir "add-confirmation-tokens.sql"

# Si no se encuentra, intentar desde el directorio actual
if (-not (Test-Path $scriptPath)) {
    $scriptPath = "scripts\add-confirmation-tokens.sql"
}

if (-not (Test-Path $scriptPath)) {
    Write-Host "ERROR: No se encuentra el archivo: add-confirmation-tokens.sql" -ForegroundColor Red
    Write-Host "Asegúrate de ejecutar el script desde el directorio raíz del proyecto." -ForegroundColor Yellow
    exit 1
}

Write-Host "Ejecutando SQL desde: $scriptPath" -ForegroundColor Yellow
Write-Host ""

# Ejecutar el SQL usando Get-Content y pipe a docker exec
try {
    Get-Content -Path $scriptPath -Raw | docker exec -i postgres-db psql -U postgres -d ucochallenge
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "============================================" -ForegroundColor Green
        Write-Host "Migración ejecutada exitosamente!" -ForegroundColor Green
        Write-Host "============================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Las columnas de tokens de confirmación han sido agregadas." -ForegroundColor Green
        Write-Host "Puedes verificar con:" -ForegroundColor Yellow
        Write-Host "  docker exec -it postgres-db psql -U postgres -d ucochallenge -c '\d usuario'" -ForegroundColor White
    } else {
        Write-Host ""
        Write-Host "============================================" -ForegroundColor Red
        Write-Host "ERROR al ejecutar la migración" -ForegroundColor Red
        Write-Host "============================================" -ForegroundColor Red
        Write-Host ""
        Write-Host "Posibles causas:" -ForegroundColor Yellow
        Write-Host "  - Las columnas ya existen (esto es normal si ya se ejecutó antes)" -ForegroundColor White
        Write-Host "  - Error de conexión a la base de datos" -ForegroundColor White
        Write-Host "  - Permisos insuficientes" -ForegroundColor White
        Write-Host ""
        Write-Host "Verifica los logs con: docker logs postgres-db" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host ""
    Write-Host "ERROR: Excepción al ejecutar la migración" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

