# Script para ejecutar la aplicación con las credenciales de base de datos
$env:DATABASE_USERNAME = "postgres"
$env:DATABASE_PASSWORD = "C2wvVCP18#6@"
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/ucochallenge"

Write-Host "Variables de entorno configuradas:"
Write-Host "  Usuario: $env:DATABASE_USERNAME"
Write-Host "  URL: $env:DATABASE_URL"
Write-Host ""
Write-Host "Ejecutando aplicación Spring Boot..."
Write-Host ""

mvn spring-boot:run

