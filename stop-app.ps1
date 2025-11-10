# Script para detener procesos que usan el puerto 8080
Write-Host "Buscando procesos en el puerto 8080..." -ForegroundColor Yellow

$port = 8080
$connections = netstat -ano | findstr ":$port" | findstr "LISTENING"

if ($connections) {
    Write-Host "`nProcesos encontrados en el puerto $port:" -ForegroundColor Cyan
    $connections | ForEach-Object {
        $parts = $_ -split '\s+'
        $pid = $parts[-1]
        $process = Get-Process -Id $pid -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "  PID: $pid - Proceso: $($process.ProcessName) - Iniciado: $($process.StartTime)" -ForegroundColor White
        } else {
            Write-Host "  PID: $pid - Proceso no encontrado (puede estar terminando)" -ForegroundColor Gray
        }
    }
    
    Write-Host "`n¿Deseas detener estos procesos? (S/N): " -ForegroundColor Yellow -NoNewline
    $response = Read-Host
    
    if ($response -eq 'S' -or $response -eq 's' -or $response -eq 'Y' -or $response -eq 'y') {
        $connections | ForEach-Object {
            $parts = $_ -split '\s+'
            $pid = $parts[-1]
            try {
                Stop-Process -Id $pid -Force -ErrorAction Stop
                Write-Host "  ✓ Proceso $pid detenido correctamente" -ForegroundColor Green
            } catch {
                Write-Host "  ✗ Error al detener el proceso $pid : $_" -ForegroundColor Red
            }
        }
        Write-Host "`nEsperando 2 segundos..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
        
        # Verificar si el puerto está libre
        $stillInUse = netstat -ano | findstr ":$port" | findstr "LISTENING"
        if ($stillInUse) {
            Write-Host "⚠ El puerto $port todavía está en uso. Puede necesitar reiniciar." -ForegroundColor Yellow
        } else {
            Write-Host "✓ El puerto $port está ahora disponible." -ForegroundColor Green
        }
    } else {
        Write-Host "Operación cancelada." -ForegroundColor Gray
    }
} else {
    Write-Host "✓ No hay procesos escuchando en el puerto $port" -ForegroundColor Green
    Write-Host "El puerto está disponible." -ForegroundColor Green
}

