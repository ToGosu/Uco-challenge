# UCO Challenge - Aplicación Autocontenida

Aplicación distribuida completa con **HTTPS**, **WAF**, **OpenTelemetry**, **Vault**, **Catálogos de Notificaciones** y más.

## Inicio Rápido (Un Solo Comando)

### Windows (PowerShell)

```powershell
.\start.ps1
```

¡Eso es todo! El script automatiza:
- Generación de certificados SSL
- Inicio de todos los servicios Docker
- Inicialización de la base de datos con datos de ejemplo
- Configuración de Vault con secretos
- Verificación de que todo funciona

## URLs Disponibles

Una vez iniciada la aplicación, puedes acceder a:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **API Backend** | https://localhost:8443/uco-challenge | API principal del backend |
| **Jaeger UI** | http://localhost:16686 | Visualización de traces (OpenTelemetry) |
| **Vault UI** | http://localhost:8200 | Gestión de secretos (Token: `root`) |
| **OTEL Metrics** | http://localhost:9090/metrics | Métricas de OpenTelemetry (Prometheus) |
| **Health Check** | https://localhost:8443/health | Estado de los servicios |

## Detener la Aplicación

### Usando el Script (Recomendado)

```powershell
# Detener servicios
.\stop.ps1

# Detener y eliminar todos los datos (elimina BD, Vault, etc.)
.\stop.ps1 -RemoveVolumes

# Detener y eliminar también las imágenes
.\stop.ps1 -RemoveVolumes -RemoveImages
```

### Usando Docker Compose Directamente

```powershell
# Detener servicios
docker-compose down

# Detener y eliminar todos los datos
docker-compose down -v
```

## 📋 Comandos Útiles

```powershell
# Ver estado de servicios
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f app
docker-compose logs -f nginx-waf

# Reiniciar un servicio
docker-compose restart app

# Reconstruir un servicio
docker-compose up -d --build app
```

## 🔧 Opciones del Script de Inicio

El script `start.ps1` acepta parámetros opcionales:

```powershell
# Saltar generación de certificados (si ya existen)
.\start.ps1 -SkipCerts

# Saltar inicialización de Vault
.\start.ps1 -SkipVault

# Saltar inicialización de datos en BD
.\start.ps1 -SkipData

# Combinar opciones
.\start.ps1 -SkipCerts -SkipVault
```

## Arquitectura

La aplicación incluye:

- **HTTPS/SSL**: Certificados autofirmados para desarrollo
- **WAF**: Nginx con reglas de seguridad básicas
- **OpenTelemetry**: Observabilidad completa con Jaeger
- **Vault**: Gestión de secretos (HashiCorp Vault)
- **Catálogos**: Servicio de notificaciones y parámetros
- **Pruebas Unitarias**: 25+ tests con JUnit y Mockito
- **Docker**: Todo containerizado y listo para ejecutar

## Documentación Completa

- **Documentación Técnica Completa**: `DOCUMENTACION-TECNICA.md` - **Recomendado para presentación**
- **Scripts**: Ver carpeta `scripts/`

## Actualizar Datos de la Base de Datos

Para actualizar los datos de ejemplo (ciudades, tipos de identificación, etc.):

```powershell
.\scripts\update-data.ps1
```

Este script:
- Actualiza o inserta datos de países, departamentos, ciudades y tipos de identificación
- Muestra un resumen de los datos actualizados
- Funciona incluso si los datos ya existen (usa `ON CONFLICT DO UPDATE`)

## Requisitos Previos

- **Docker Desktop** instalado y corriendo
- **Git Bash** (para scripts bash, opcional)
- **PowerShell** 5.1 o superior

## Solución de Problemas

### Error: Docker no está corriendo
```powershell
# Inicia Docker Desktop y espera a que esté listo
# Luego ejecuta nuevamente:
.\start.ps1
```

### Error: Certificados no encontrados
```powershell
# Genera los certificados manualmente:
bash scripts/generate-certs.sh
```

### Error: Puerto ya en uso
```powershell
# Ver qué está usando el puerto
netstat -ano | findstr :8443

# Detener servicios Docker
docker-compose down
```

### Ver logs de errores
```powershell
# Ver todos los logs
docker-compose logs

# Ver logs de un servicio específico
docker-compose logs app
docker-compose logs nginx-waf
```

## Próximos Pasos

1. **Aceptar certificado SSL**: La primera vez que accedas a `https://localhost:8443`, acepta el certificado autofirmado
2. **Probar la API**: Visita `https://localhost:8443/uco-challenge` para ver información de la API
3. **Ver traces**: Abre Jaeger en `http://localhost:16686` para ver la observabilidad
4. **Configurar frontend**: Actualiza la URL del frontend a `https://localhost:8443/uco-challenge`

## Notas

- Los certificados SSL son **autofirmados** (solo para desarrollo)
- Vault está en **modo desarrollo** (no usar en producción)
- La base de datos se inicializa automáticamente con datos de ejemplo
- Todos los servicios tienen health checks configurados

## Contribuir

Para más información sobre la arquitectura y configuración, consulta:
- `GUIA-INICIO-RAPIDO.md` - Guía detallada
- `docker-compose.yml` - Configuración de servicios
- `nginx/default.conf` - Configuración del WAF

---

**¿Problemas?** Revisa los logs con `docker-compose logs -f` o consulta la documentación completa.

