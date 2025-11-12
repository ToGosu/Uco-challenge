# Script para verificar que la migración se ejecutó correctamente
# Verifica que las columnas de tokens existan en la tabla usuario

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Verificando migración de tokens" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que el contenedor está corriendo
$containerRunning = docker ps --filter "name=postgres-db" --format "{{.Names}}"

if (-not $containerRunning) {
    Write-Host "ERROR: El contenedor 'postgres-db' no está en ejecución." -ForegroundColor Red
    exit 1
}

Write-Host "Verificando columnas en la tabla 'usuario'..." -ForegroundColor Yellow
Write-Host ""

# Consulta SQL para verificar las columnas
$verifySQL = @"
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'usuario'
  AND column_name IN (
    'token_confirmacion_email',
    'token_email_expiracion',
    'token_confirmacion_movil',
    'token_movil_expiracion'
  )
ORDER BY column_name;
"@

# Ejecutar la verificación
Write-Host "Ejecutando verificación..." -ForegroundColor Yellow
Write-Host ""

$result = $verifySQL | docker exec -i postgres-db psql -U postgres -d ucochallenge -t

if ($LASTEXITCODE -eq 0) {
    Write-Host "Resultado:" -ForegroundColor Green
    Write-Host $result -ForegroundColor White
    Write-Host ""
    
    # Contar las columnas encontradas
    $columnCount = ($result -split "`n" | Where-Object { $_.Trim() -ne "" }).Count
    
    if ($columnCount -ge 4) {
        Write-Host "============================================" -ForegroundColor Green
        Write-Host "Migración verificada correctamente!" -ForegroundColor Green
        Write-Host "Todas las columnas están presentes." -ForegroundColor Green
        Write-Host "============================================" -ForegroundColor Green
    } else {
        Write-Host "============================================" -ForegroundColor Yellow
        Write-Host "ADVERTENCIA: No se encontraron todas las columnas." -ForegroundColor Yellow
        Write-Host "Columnas encontradas: $columnCount de 4" -ForegroundColor Yellow
        Write-Host "============================================" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Ejecuta la migración con: .\scripts\run-migration-simple.ps1" -ForegroundColor Yellow
    }
} else {
    Write-Host "ERROR al verificar la migración" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Para ver la estructura completa de la tabla:" -ForegroundColor Yellow
Write-Host "  docker exec -it postgres-db psql -U postgres -d ucochallenge -c '\d usuario'" -ForegroundColor White

