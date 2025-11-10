# Dockerfile para Nginx con ModSecurity
# Usa imagen base de Nginx estándar
# Para desarrollo: ModSecurity se simula con reglas de Nginx
# Para producción: usar imagen con ModSecurity compilado

FROM nginx:1.25-alpine

# Instalar herramientas necesarias
RUN apk add --no-cache wget curl

# Copiar configuraciones personalizadas
COPY nginx.conf /etc/nginx/nginx.conf
COPY modsecurity.conf /etc/nginx/modsecurity.conf
COPY default.conf /etc/nginx/conf.d/default.conf

# Crear directorios para logs y certificados
RUN mkdir -p /var/log/nginx /var/log/modsec /etc/nginx/ssl && \
    chmod -R 755 /var/log/nginx /var/log/modsec

# Exponer puertos HTTP y HTTPS
EXPOSE 80 443 8443

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:80/health || exit 1

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]

# NOTA: Para habilitar ModSecurity completo en producción:
# 1. Usar imagen: FROM owasp/modsecurity-crs:nginx-alpine
# 2. O compilar ModSecurity como módulo dinámico de Nginx
# 3. Descomentar líneas de ModSecurity en nginx.conf

