param(
    [switch]$RemoveVolumes,
    [switch]$RemoveImages
)

Write-Host ""
Write-Host "Deteniendo UCO Challenge..." -ForegroundColor Yellow
Write-Host ""

if ($RemoveVolumes) {
    Write-Host "ADVERTENCIA: Se eliminarán todos los volúmenes (datos de BD, Vault, etc.)" -ForegroundColor Red
    Write-Host "   Esto eliminará permanentemente todos los datos almacenados." -ForegroundColor Red
    Write-Host ""
    $confirm = Read-Host "¿Estás seguro? (escribe 'SI' para confirmar)"
    if ($confirm -ne "SI") {
        Write-Host "Operación cancelada." -ForegroundColor Gray
        exit 0
    }
    Write-Host ""
    Write-Host "Deteniendo servicios y eliminando volúmenes..." -ForegroundColor Yellow
    docker-compose down -v
} else {
    Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
    docker-compose down
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "Servicios detenidos correctamente" -ForegroundColor Green
} else {
    Write-Host "Algunos servicios pueden no haberse detenido correctamente" -ForegroundColor Yellow
}

if ($RemoveImages) {
    Write-Host ""
    Write-Host "Eliminando imágenes..." -ForegroundColor Yellow
    docker-compose down --rmi all
    Write-Host "Imágenes eliminadas" -ForegroundColor Green
}

Write-Host ""
Write-Host "Para iniciar nuevamente: .\start.ps1" -ForegroundColor Cyan
Write-Host ""

