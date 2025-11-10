# Implementación de WAF, OpenTelemetry y HTTPS

Este documento describe la implementación de Web Application Firewall (WAF), OpenTelemetry y protocolo HTTPS en el proyecto Uco-challenge.

## Arquitectura

```
Internet → Nginx WAF (HTTPS) → Spring Boot Services (OpenTelemetry) → OTEL Collector → Jaeger
```

## Componentes Implementados

### 1. WAF (Web Application Firewall)
- **Nginx** con **ModSecurity** como reverse proxy
- Protección contra ataques comunes (SQL Injection, XSS, Command Injection)
- Modo de detección (registra pero no bloquea por defecto)
- Logging de intentos de ataque en `/var/log/modsec/`

### 2. OpenTelemetry
- **OpenTelemetry Java Agent** para instrumentación automática
- **OTEL Collector** para recopilar y exportar traces
- **Jaeger** para visualización de traces distribuidos
- Métricas y logs integrados

### 3. HTTPS
- Certificados SSL/TLS autofirmados para desarrollo
- SSL termination en Nginx
- Redirección automática de HTTP a HTTPS

## Estructura de Archivos

```
Uco-challenge/
├── nginx/
│   ├── Dockerfile              # Nginx con ModSecurity
│   ├── nginx.conf              # Configuración principal
│   ├── modsecurity.conf        # Reglas ModSecurity
│   └── default.conf            # Virtual hosts con SSL
├── otel-collector/
│   └── otel-collector-config.yml  # Configuración OTEL Collector
├── scripts/
│   └── generate-certs.sh      # Script para generar certificados
├── certs/                      # Certificados SSL (gitignored)
├── templates/                  # Plantillas para otros servicios
│   ├── spring-boot-otel-config.yml
│   ├── nginx-service-config.conf
│   ├── docker-compose-service-template.yml
│   └── Dockerfile-template
└── docker-compose.yml          # Servicios actualizados
```

## Instalación y Configuración

### 1. Generar Certificados SSL

```bash
# Dar permisos de ejecución al script
chmod +x scripts/generate-certs.sh

# Ejecutar script (requiere OpenSSL)
./scripts/generate-certs.sh
```

Esto generará:
- Certificado CA (`certs/ca.crt`, `certs/ca.key`)
- Certificados para cada servicio (backend, api-gateway, catalog-service, nginx-waf)

### 2. Construir y Levantar Servicios

```bash
# Construir e iniciar todos los servicios
docker-compose up -d --build

# Ver logs
docker-compose logs -f

# Verificar estado
docker-compose ps
```

### 3. Verificar Funcionamiento

#### HTTPS
```bash
# Verificar redirección HTTP → HTTPS
curl -L http://localhost:80/health

# Verificar HTTPS (ignorar certificado autofirmado)
curl -k https://localhost:8443/health

# Acceder al backend
curl -k https://localhost:8443/uco-challenge/api/v1/cities
```

#### OpenTelemetry / Jaeger
```bash
# Abrir Jaeger UI en el navegador
http://localhost:16686

# Buscar traces del servicio "uco-challenge-backend"
```

#### WAF / ModSecurity
```bash
# Ver logs de ModSecurity
docker logs nginx-waf

# Probar detección de SQL Injection (debe aparecer en logs)
curl -k "https://localhost:8443/uco-challenge/api/v1/test?id=1' OR '1'='1"
```

## Puertos Expuestos

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Nginx WAF | 80 | HTTP (redirige a HTTPS) |
| Nginx WAF | 8443 | HTTPS |
| Backend | 8080 | Interno (detrás de Nginx) |
| OTEL Collector | 4317 | OTLP gRPC |
| OTEL Collector | 4318 | OTLP HTTP |
| Jaeger UI | 16686 | Interfaz web |
| Vault | 8200 | Gestión de secretos |
| PostgreSQL | 5432 | Base de datos (interno) |

## Configuración de Servicios Adicionales

Para implementar WAF, OpenTelemetry y HTTPS en **API Gateway** y **Catalog Service**, seguir las instrucciones en `templates/README.md`.

### Pasos Rápidos:

1. **Copiar Dockerfile template:**
   ```bash
   cp templates/Dockerfile-template <servicio>/Dockerfile
   # Editar: OTEL_SERVICE_NAME, puerto EXPOSE
   ```

2. **Agregar configuración OpenTelemetry a application.yml:**
   ```yaml
   # Copiar contenido de templates/spring-boot-otel-config.yml
   ```

3. **Agregar servicio a docker-compose.yml:**
   ```yaml
   # Copiar desde templates/docker-compose-service-template.yml
   ```

4. **Actualizar nginx/default.conf:**
   ```nginx
   # Agregar location block desde templates/nginx-service-config.conf
   ```

## Variables de Entorno OpenTelemetry

Los servicios Spring Boot usan estas variables (configuradas en docker-compose.yml):

- `OTEL_SERVICE_NAME`: Nombre del servicio
- `OTEL_EXPORTER_OTLP_ENDPOINT`: Endpoint del OTEL Collector
- `OTEL_EXPORTER_OTLP_PROTOCOL`: Protocolo (grpc o http)
- `OTEL_RESOURCE_ATTRIBUTES`: Atributos del recurso
- `OTEL_TRACES_EXPORTER`: Exportador de traces (otlp)
- `OTEL_METRICS_EXPORTER`: Exportador de métricas (otlp)
- `OTEL_LOGS_EXPORTER`: Exportador de logs (otlp)

## Reglas ModSecurity

Las reglas básicas implementadas detectan:

- **SQL Injection** (ID: 1001)
- **XSS** (ID: 1002)
- **Command Injection** (ID: 1003)

Los endpoints `/health` y `/actuator` están excluidos de las reglas.

Para cambiar el modo de ModSecurity (detección → bloqueo), editar `nginx/modsecurity.conf`:

```nginx
# Cambiar de DetectionOnly a On
SecRuleEngine On
```

## Troubleshooting

### Certificados SSL no encontrados
```bash
# Verificar que los certificados existen
ls -la certs/

# Regenerar si es necesario
./scripts/generate-certs.sh
```

### Nginx no inicia
```bash
# Ver logs de Nginx
docker logs nginx-waf

# Verificar configuración
docker exec nginx-waf nginx -t
```

### OpenTelemetry no envía traces
```bash
# Verificar que OTEL Collector está corriendo
docker ps | grep otel-collector

# Ver logs del servicio
docker logs <nombre-servicio>

# Verificar variables de entorno
docker exec <nombre-servicio> env | grep OTEL
```

### Jaeger no muestra traces
```bash
# Verificar que Jaeger está corriendo
docker ps | grep jaeger

# Verificar conexión OTEL Collector → Jaeger
docker logs otel-collector
```

## Seguridad

⚠️ **IMPORTANTE PARA PRODUCCIÓN:**

1. **Certificados:** Reemplazar certificados autofirmados por certificados válidos (Let's Encrypt, CA comercial)
2. **ModSecurity:** Cambiar a modo bloqueo y ajustar reglas según necesidades
3. **Secrets:** No hardcodear contraseñas en docker-compose.yml, usar Vault
4. **Redes:** Asegurar que servicios internos no estén expuestos públicamente
5. **Logs:** Configurar rotación de logs y almacenamiento seguro

## Referencias

- [ModSecurity Documentation](https://github.com/SpiderLabs/ModSecurity)
- [OpenTelemetry Java](https://opentelemetry.io/docs/instrumentation/java/)
- [Nginx SSL Configuration](https://nginx.org/en/docs/http/configuring_https_servers.html)
- [Jaeger Documentation](https://www.jaegertracing.io/docs/)

