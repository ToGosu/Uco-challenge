# Script para eliminar todos los usuarios de la base de datos
# Ejecutar: .\scripts\delete-all-users.ps1

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Eliminando todos los usuarios de la base de datos" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que Docker está corriendo
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Docker no responde"
    }
} catch {
    Write-Host "ERROR: Docker no está instalado o no está corriendo." -ForegroundColor Red
    Write-Host "   Por favor inicia Docker Desktop e inténtalo de nuevo." -ForegroundColor Yellow
    exit 1
}

# Verificar que el contenedor de PostgreSQL está corriendo
Write-Host "Verificando contenedor de PostgreSQL..." -ForegroundColor Yellow
$postgresRunning = docker ps --filter "name=postgres-db" --format "{{.Names}}" 2>&1

if (-not $postgresRunning -or $postgresRunning -notmatch "postgres-db") {
    Write-Host "El contenedor de PostgreSQL no está corriendo." -ForegroundColor Yellow
    Write-Host "   Iniciando servicios..." -ForegroundColor Gray
    docker-compose up -d db
    Start-Sleep -Seconds 10
}

Write-Host "Contenedor encontrado: $postgresRunning" -ForegroundColor Green
Write-Host ""

# Esperar a que PostgreSQL esté listo
Write-Host "Esperando a que PostgreSQL esté listo..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0
$postgresReady = $false

do {
    Start-Sleep -Seconds 2
    $attempt++
    $result = docker exec postgres-db pg_isready -U postgres 2>&1
    if ($LASTEXITCODE -eq 0) {
        $postgresReady = $true
    }
} while (-not $postgresReady -and $attempt -lt $maxAttempts)

if (-not $postgresReady) {
    Write-Host "ERROR: PostgreSQL no está disponible después de $maxAttempts intentos." -ForegroundColor Red
    exit 1
}

Write-Host "PostgreSQL está listo" -ForegroundColor Green
Write-Host ""

# Verificar que el archivo SQL existe
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlPath = Join-Path $scriptDir "delete-all-users.sql"

if (-not (Test-Path $sqlPath)) {
    $sqlPath = "scripts\delete-all-users.sql"
}

if (-not (Test-Path $sqlPath)) {
    Write-Host "ERROR: No se encuentra el archivo: delete-all-users.sql" -ForegroundColor Red
    Write-Host "Asegúrate de ejecutar el script desde el directorio raíz del proyecto." -ForegroundColor Yellow
    exit 1
}

Write-Host "Archivo SQL encontrado: $sqlPath" -ForegroundColor Green
Write-Host ""

# Mostrar advertencia
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "ADVERTENCIA: Se eliminarán TODOS los usuarios" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

# Confirmar antes de eliminar
$confirmation = Read-Host "¿Estás seguro de que deseas eliminar todos los usuarios? (escribe 'SI' para confirmar)"

if ($confirmation -ne "SI") {
    Write-Host ""
    Write-Host "Operación cancelada. No se eliminó ningún usuario." -ForegroundColor Yellow
    exit 0
}

Write-Host ""
Write-Host "Ejecutando script de eliminación..." -ForegroundColor Yellow

try {
    # Ejecutar el SQL
    Get-Content $sqlPath | docker exec -i postgres-db psql -U postgres -d ucochallenge
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "============================================" -ForegroundColor Green
        Write-Host "Usuarios eliminados correctamente" -ForegroundColor Green
        Write-Host "============================================" -ForegroundColor Green
        Write-Host ""
        
        # Mostrar resumen final
        Write-Host "Verificando estado final de la tabla usuario..." -ForegroundColor Cyan
        docker exec postgres-db psql -U postgres -d ucochallenge -c "SELECT COUNT(*) as usuarios_restantes FROM usuario;"
        
    } else {
        Write-Host ""
        Write-Host "ERROR: Hubo un problema al eliminar los usuarios." -ForegroundColor Red
        Write-Host "Revisa los logs para más información." -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host ""
    Write-Host "ERROR: Excepción al ejecutar el script: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Proceso completado." -ForegroundColor Green
Write-Host ""

