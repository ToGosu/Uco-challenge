# Script para ejecutar migraciones SQL en el contenedor de PostgreSQL
# Uso: .\scripts\run-migration.ps1 -ScriptFile "scripts\add-confirmation-tokens.sql"

param(
    [Parameter(Mandatory=$false)]
    [string]$ScriptFile = "scripts\add-confirmation-tokens.sql",
    
    [Parameter(Mandatory=$false)]
    [string]$ContainerName = "postgres-db",
    
    [Parameter(Mandatory=$false)]
    [string]$Database = "ucochallenge",
    
    [Parameter(Mandatory=$false)]
    [string]$User = "postgres"
)

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Ejecutando migración SQL en PostgreSQL" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que el archivo existe
if (-not (Test-Path $ScriptFile)) {
    Write-Host "ERROR: El archivo '$ScriptFile' no existe." -ForegroundColor Red
    Write-Host "Verifica la ruta del archivo e intenta nuevamente." -ForegroundColor Red
    exit 1
}

Write-Host "Archivo SQL: $ScriptFile" -ForegroundColor Yellow
Write-Host "Contenedor: $ContainerName" -ForegroundColor Yellow
Write-Host "Base de datos: $Database" -ForegroundColor Yellow
Write-Host "Usuario: $User" -ForegroundColor Yellow
Write-Host ""

# Verificar que el contenedor está en ejecución
Write-Host "Verificando que el contenedor está en ejecución..." -ForegroundColor Yellow
$containerStatus = docker ps --filter "name=$ContainerName" --format "{{.Status}}"

if (-not $containerStatus) {
    Write-Host "ERROR: El contenedor '$ContainerName' no está en ejecución." -ForegroundColor Red
    Write-Host "Inicia el contenedor con: docker-compose up -d db" -ForegroundColor Yellow
    exit 1
}

Write-Host "Contenedor encontrado: $containerStatus" -ForegroundColor Green
Write-Host ""

# Leer el contenido del archivo SQL
Write-Host "Leyendo archivo SQL..." -ForegroundColor Yellow
$sqlContent = Get-Content -Path $ScriptFile -Raw -Encoding UTF8

if (-not $sqlContent) {
    Write-Host "ERROR: El archivo SQL está vacío." -ForegroundColor Red
    exit 1
}

Write-Host "Archivo SQL leído correctamente." -ForegroundColor Green
Write-Host ""

# Ejecutar el SQL en el contenedor
Write-Host "Ejecutando migración SQL..." -ForegroundColor Yellow
Write-Host ""

try {
    # Ejecutar el SQL usando docker exec
    $result = docker exec -i $ContainerName psql -U $User -d $Database -c $sqlContent 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "============================================" -ForegroundColor Green
        Write-Host "Migración ejecutada exitosamente!" -ForegroundColor Green
        Write-Host "============================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Resultado:" -ForegroundColor Yellow
        Write-Host $result -ForegroundColor White
    } else {
        Write-Host "============================================" -ForegroundColor Red
        Write-Host "ERROR al ejecutar la migración" -ForegroundColor Red
        Write-Host "============================================" -ForegroundColor Red
        Write-Host ""
        Write-Host "Salida del comando:" -ForegroundColor Yellow
        Write-Host $result -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "ERROR: Excepción al ejecutar la migración" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Migración completada!" -ForegroundColor Green

