param(
    [switch]$SkipCerts,
    [switch]$SkipVault,
    [switch]$SkipData
)

$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "Iniciando UCO Challenge - Aplicación Autocontenida" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Paso 1/8: Verificando Docker..." -ForegroundColor Yellow
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Docker no responde"
    }
    Write-Host "   Docker está funcionando" -ForegroundColor Green
} catch {
    Write-Host "   Docker no está instalado o no está corriendo." -ForegroundColor Red
    Write-Host "   Por favor instala Docker Desktop e inícialo." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Paso 2/8: Verificando certificados SSL..." -ForegroundColor Yellow
if (-not $SkipCerts) {
    if (-not (Test-Path "certs/nginx-waf.crt")) {
        Write-Host "   Certificados no encontrados. Generando..." -ForegroundColor Yellow
        if (Get-Command bash -ErrorAction SilentlyContinue) {
            bash scripts/generate-certs.sh
            if ($LASTEXITCODE -eq 0) {
                Write-Host "   Certificados generados correctamente" -ForegroundColor Green
            } else {
                Write-Host "   Error al generar certificados. Continuando..." -ForegroundColor Yellow
            }
        } else {
            Write-Host "   Git Bash no encontrado. Genera los certificados manualmente:" -ForegroundColor Yellow
            Write-Host "      bash scripts/generate-certs.sh" -ForegroundColor Gray
        }
    } else {
        Write-Host "   Certificados ya existen" -ForegroundColor Green
    }
} else {
    Write-Host "   Saltando generación de certificados (--SkipCerts)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Paso 3/8: Verificando proyectos opcionales..." -ForegroundColor Yellow
$apiGatewayPath = "C:\UCO\IS2\Workspace-is2-2025\apigateway"
$catalogServicePath = "C:\UCO\IS2\Workspace-is2-2025\catalogservice"
$frontendPath = "C:\Users\santi\Desktop\uco-challenge-fr"

$missingProjects = @()
if (-not (Test-Path $apiGatewayPath)) {
    $missingProjects += "API Gateway"
    Write-Host "   API Gateway no encontrado (opcional)" -ForegroundColor Yellow
}
if (-not (Test-Path $catalogServicePath)) {
    $missingProjects += "Catalog Service"
    Write-Host "   Catalog Service no encontrado (opcional)" -ForegroundColor Yellow
}
if (-not (Test-Path $frontendPath)) {
    $missingProjects += "Frontend"
    Write-Host "   Frontend no encontrado (opcional)" -ForegroundColor Yellow
}

if ($missingProjects.Count -eq 0) {
    Write-Host "   Todos los proyectos encontrados" -ForegroundColor Green
} else {
    Write-Host "   Algunos proyectos opcionales no están disponibles" -ForegroundColor Gray
    Write-Host "      La aplicación funcionará sin ellos" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Paso 4/8: Iniciando servicios Docker..." -ForegroundColor Yellow
Write-Host "   Esto puede tardar varios minutos en la primera ejecución..." -ForegroundColor Gray
docker-compose up -d --build

if ($LASTEXITCODE -ne 0) {
    Write-Host "   Error al iniciar servicios Docker" -ForegroundColor Red
    Write-Host "   Revisa los logs: docker-compose logs" -ForegroundColor Yellow
    exit 1
}

Write-Host "   Servicios iniciados" -ForegroundColor Green

Write-Host ""
Write-Host "Paso 5/8: Esperando a que los servicios estén listos..." -ForegroundColor Yellow

Write-Host "   Esperando PostgreSQL..." -ForegroundColor Gray
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

if ($postgresReady) {
    Write-Host "   PostgreSQL listo" -ForegroundColor Green
} else {
    Write-Host "   PostgreSQL tardó mucho en iniciar, continuando..." -ForegroundColor Yellow
}

Write-Host "   Esperando Vault..." -ForegroundColor Gray
$attempt = 0
$vaultReady = $false
do {
    Start-Sleep -Seconds 2
    $attempt++
    $result = docker exec vault sh -c "VAULT_ADDR=http://127.0.0.1:8200 vault status" 2>&1
    if ($LASTEXITCODE -eq 0) {
        $vaultReady = $true
    }
} while (-not $vaultReady -and $attempt -lt $maxAttempts)

if ($vaultReady) {
    Write-Host "   Vault listo" -ForegroundColor Green
} else {
    Write-Host "   Vault tardó mucho en iniciar, continuando..." -ForegroundColor Yellow
}

Start-Sleep -Seconds 5

Write-Host ""
Write-Host "Paso 6/8: Inicializando base de datos con datos de ejemplo..." -ForegroundColor Yellow
if (-not $SkipData) {
    if (Test-Path "scripts/init-data.sql") {
        try {
            Get-Content scripts/init-data.sql | docker exec -i postgres-db psql -U postgres -d ucochallenge 2>&1 | Out-Null
            Write-Host "   Datos inicializados en la base de datos" -ForegroundColor Green
        } catch {
            Write-Host "   Error al inicializar datos (puede que ya existan)" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   Script de inicialización no encontrado" -ForegroundColor Yellow
    }
} else {
    Write-Host "   Saltando inicialización de datos (--SkipData)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Paso 7/8: Inicializando Vault con secretos..." -ForegroundColor Yellow
if (-not $SkipVault) {
    Write-Host "   Intentando inicialización automática con contenedor..." -ForegroundColor Gray
    docker-compose --profile init up -d vault-init 2>&1 | Out-Null
    
    Start-Sleep -Seconds 5
    
    $vaultInitStatus = docker ps -a --filter "name=vault-init" --format "{{.Status}}" 2>&1
    if ($vaultInitStatus -match "Exited") {
        Write-Host "   Vault inicializado automáticamente" -ForegroundColor Green
    } else {
        if (Test-Path "vault-config/init-secrets.sh") {
            if (Get-Command bash -ErrorAction SilentlyContinue) {
                Write-Host "   Intentando inicialización manual..." -ForegroundColor Gray
                $env:VAULT_ADDR = "http://localhost:8200"
                $env:VAULT_TOKEN = "root"
                try {
                    bash vault-config/init-secrets.sh 2>&1 | Out-Null
                    Write-Host "   Vault inicializado con secretos" -ForegroundColor Green
                } catch {
                    Write-Host "   Error al inicializar Vault (puede que ya esté inicializado)" -ForegroundColor Yellow
                }
            } else {
                Write-Host "   Git Bash no encontrado. Vault puede no estar inicializado" -ForegroundColor Yellow
                Write-Host "      Inicializa manualmente: bash vault-config/init-secrets.sh" -ForegroundColor Gray
            }
        }
    }
} else {
    Write-Host "   Saltando inicialización de Vault (--SkipVault)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Paso 8/8: Esperando a que la aplicación esté lista..." -ForegroundColor Yellow
Write-Host "   Esto puede tardar 30-60 segundos..." -ForegroundColor Gray
Start-Sleep -Seconds 30

Write-Host ""
Write-Host "Verificando estado de servicios..." -ForegroundColor Yellow
docker-compose ps

Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Aplicación iniciada correctamente!" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "URLs disponibles:" -ForegroundColor Cyan
Write-Host "   API Backend:     https://localhost:8443/uco-challenge" -ForegroundColor White
Write-Host "   Jaeger UI:       http://localhost:16686" -ForegroundColor White
Write-Host "   Vault UI:        http://localhost:8200 (Token: root)" -ForegroundColor White
Write-Host "   OTEL Metrics:    http://localhost:8888/metrics" -ForegroundColor White
Write-Host ""
Write-Host "Comandos útiles:" -ForegroundColor Cyan
Write-Host "   Ver logs:           docker-compose logs -f" -ForegroundColor Gray
Write-Host "   Ver estado:         docker-compose ps" -ForegroundColor Gray
Write-Host "   Detener:            docker-compose down" -ForegroundColor Gray
Write-Host "   Reiniciar:          docker-compose restart" -ForegroundColor Gray
Write-Host ""
Write-Host "Tip: Acepta el certificado autofirmado en el navegador la primera vez" -ForegroundColor Yellow
Write-Host ""

