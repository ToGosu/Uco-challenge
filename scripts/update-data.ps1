Write-Host ""
Write-Host "Actualizando datos en la base de datos..." -ForegroundColor Cyan
Write-Host ""

try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Docker no responde"
    }
} catch {
    Write-Host "Docker no está instalado o no está corriendo." -ForegroundColor Red
    Write-Host "   Por favor inicia Docker Desktop e inícialo." -ForegroundColor Yellow
    exit 1
}

$postgresRunning = docker ps --filter "name=postgres-db" --format "{{.Names}}" 2>&1
if (-not $postgresRunning -or $postgresRunning -notmatch "postgres-db") {
    Write-Host "El contenedor de PostgreSQL no está corriendo." -ForegroundColor Yellow
    Write-Host "   Iniciando servicios..." -ForegroundColor Gray
    docker-compose up -d db
    Start-Sleep -Seconds 10
}

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
    Write-Host "PostgreSQL no está disponible después de $maxAttempts intentos." -ForegroundColor Red
    exit 1
}

Write-Host "PostgreSQL está listo" -ForegroundColor Green
Write-Host ""

Write-Host "Ejecutando script de actualizacion..." -ForegroundColor Yellow
try {
    Get-Content scripts/update-data.sql | docker exec -i postgres-db psql -U postgres -d ucochallenge
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "Datos actualizados correctamente" -ForegroundColor Green
        Write-Host ""
        Write-Host "Resumen de datos:" -ForegroundColor Cyan
        docker exec postgres-db psql -U postgres -d ucochallenge -c "SELECT 'Pais' as tipo, COUNT(*) as cantidad FROM pais UNION ALL SELECT 'Departamento', COUNT(*) FROM departamento UNION ALL SELECT 'Ciudad', COUNT(*) FROM ciudad UNION ALL SELECT 'Tipo Identificacion', COUNT(*) FROM tipo_identificacion;"
    } else {
        Write-Host "Hubo un error al actualizar los datos. Revisa los logs." -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error al ejecutar el script: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

