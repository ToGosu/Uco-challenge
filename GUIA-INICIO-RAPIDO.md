# Guía de Inicio Rápido - WAF, OpenTelemetry y HTTPS

Esta guía te ayudará a poner en marcha el proyecto con todas las nuevas funcionalidades.

## Prerrequisitos

- ✅ Docker y Docker Compose instalados
- ✅ OpenSSL instalado (para generar certificados)
- ✅ Git (opcional, para clonar repositorios)

## Pasos para Ejecutar el Proyecto

### Paso 1: Generar Certificados SSL

Los certificados son necesarios para HTTPS. Ejecuta el script de generación:

```bash
# En Windows (PowerShell)
# Dar permisos de ejecución (si es necesario)
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Ejecutar script (requiere Git Bash o WSL si estás en Windows)
bash scripts/generate-certs.sh

# O en Linux/Mac
chmod +x scripts/generate-certs.sh
./scripts/generate-certs.sh
```

**Verificar que se generaron los certificados:**
```bash
ls certs/
# Deberías ver: ca.crt, ca.key, backend.crt, backend.key, nginx-waf.crt, nginx-waf.key, etc.
```

### Paso 2: Inicializar Vault (Opcional)

Si quieres usar Vault para secretos:

```bash
# Esperar a que Vault esté corriendo (después del docker-compose up)
# Luego ejecutar:
bash vault-config/init-secrets.sh
```

### Paso 3: Construir e Iniciar Servicios

```bash
# Construir todas las imágenes e iniciar servicios
docker-compose up -d --build

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f app
docker-compose logs -f nginx-waf
docker-compose logs -f otel-collector
```

### Paso 4: Verificar que Todo Está Funcionando

#### 4.1 Verificar Estado de los Servicios

```bash
# Ver estado de todos los servicios
docker-compose ps

# Deberías ver todos los servicios como "Up" o "healthy"
```

#### 4.2 Verificar HTTPS

```bash
# Probar redirección HTTP → HTTPS
curl -L http://localhost:80/health

# Probar HTTPS directamente (ignorar certificado autofirmado)
curl -k https://localhost:8443/health

# Probar endpoint del backend
curl -k https://localhost:8443/uco-challenge/api/v1/cities
```

#### 4.3 Verificar OpenTelemetry / Jaeger

1. Abre tu navegador en: **http://localhost:16686**
2. En el menú desplegable, selecciona el servicio: **uco-challenge-backend**
3. Haz clic en "Find Traces"
4. Deberías ver traces de las peticiones

#### 4.4 Verificar Base de Datos

```bash
# Conectarse a PostgreSQL
docker exec -it postgres-db psql -U postgres -d ucochallenge

# Ver tablas
\dt

# Salir
\q
```

#### 4.5 Verificar Vault

```bash
# Verificar estado de Vault
docker exec vault vault status

# Listar secretos (si están configurados)
docker exec vault vault kv list secret/
```

### Paso 5: Probar el Frontend

Si tienes el frontend Vue corriendo:

1. **Actualizar la URL de la API** en el frontend a: `https://localhost:8443/uco-challenge`
2. **Aceptar el certificado autofirmado** en el navegador (primera vez)
3. Probar autenticación con Auth0

Ver `FRONTEND-CONFIGURATION.md` para más detalles.

## Comandos Útiles

### Gestión de Servicios

```bash
# Iniciar servicios
docker-compose up -d

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (⚠️ borra datos)
docker-compose down -v

# Reiniciar un servicio específico
docker-compose restart app

# Reconstruir un servicio específico
docker-compose up -d --build app
```

### Ver Logs

```bash
# Todos los servicios
docker-compose logs -f

# Servicio específico
docker-compose logs -f nginx-waf
docker-compose logs -f app
docker-compose logs -f otel-collector

# Últimas 100 líneas
docker-compose logs --tail=100 app
```

### Acceder a Contenedores

```bash
# Entrar al contenedor del backend
docker exec -it spring-app sh

# Entrar al contenedor de Nginx
docker exec -it nginx-waf sh

# Entrar a PostgreSQL
docker exec -it postgres-db psql -U postgres -d ucochallenge
```

### Verificar Health Checks

```bash
# Backend
curl http://localhost:8080/actuator/health

# Nginx
curl http://localhost:80/health

# OTEL Collector
curl http://localhost:8888/metrics

# Jaeger
curl http://localhost:16686
```

## Puertos y URLs

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Nginx WAF (HTTP)** | http://localhost:80 | Redirige a HTTPS |
| **Nginx WAF (HTTPS)** | https://localhost:8443 | Punto de entrada principal |
| **Backend API** | https://localhost:8443/uco-challenge | API del backend |
| **Jaeger UI** | http://localhost:16686 | Visualización de traces |
| **Vault UI** | http://localhost:8200 | Gestión de secretos |
| **OTEL Collector** | http://localhost:8888/metrics | Métricas |

## Troubleshooting

### Error: Certificados no encontrados

```bash
# Regenerar certificados
./scripts/generate-certs.sh

# Verificar que existen
ls -la certs/
```

### Error: Puerto ya en uso

```bash
# Ver qué está usando el puerto
# Windows
netstat -ano | findstr :8443

# Linux/Mac
lsof -i :8443

# Detener el proceso o cambiar el puerto en docker-compose.yml
```

### Error: Nginx no inicia

```bash
# Ver logs de Nginx
docker-compose logs nginx-waf

# Verificar configuración
docker exec nginx-waf nginx -t
```

### Error: Base de datos no conecta

```bash
# Verificar que PostgreSQL está corriendo
docker-compose ps db

# Ver logs
docker-compose logs db

# Reiniciar
docker-compose restart db
```

### Error: OpenTelemetry no envía traces

```bash
# Verificar variables de entorno
docker exec spring-app env | grep OTEL

# Ver logs del OTEL Collector
docker-compose logs otel-collector

# Verificar que Jaeger está corriendo
docker-compose ps jaeger
```

### El frontend no puede conectar

1. Verificar que la URL es `https://localhost:8443/uco-challenge`
2. Aceptar el certificado autofirmado en el navegador
3. Verificar CORS en los logs del backend
4. Verificar que Nginx está corriendo: `docker-compose ps nginx-waf`

## Siguiente Paso: Integrar API Gateway y Catalog Service

Si aún no has integrado los otros servicios, sigue las instrucciones en:
- `templates/README.md`

## Verificación Final

Ejecuta este script para verificar que todo está funcionando:

```bash
# Health checks
echo "🔍 Verificando servicios..."
curl -s http://localhost:80/health && echo " ✅ Nginx HTTP"
curl -sk https://localhost:8443/health && echo " ✅ Nginx HTTPS"
curl -s http://localhost:8080/actuator/health && echo " ✅ Backend"
curl -s http://localhost:16686 > /dev/null && echo " ✅ Jaeger"
curl -s http://localhost:8888/metrics > /dev/null && echo " ✅ OTEL Collector"
echo "✅ Todos los servicios están funcionando!"
```

## ¿Problemas?

1. Revisa los logs: `docker-compose logs -f`
2. Verifica el estado: `docker-compose ps`
3. Consulta `README-WAF-OTEL-HTTPS.md` para más detalles
4. Revisa la configuración de cada servicio

