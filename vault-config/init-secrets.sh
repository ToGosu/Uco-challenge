#!/bin/bash

export VAULT_ADDR='http://localhost:8200'
export VAULT_TOKEN='dev-root-token-id'

echo "🔐 Inicializando Vault con secretos..."

# Esperar a que Vault esté disponible
echo "⏳ Esperando a que Vault esté listo..."
sleep 5

# Habilitar secrets engine v2
vault secrets enable -path=secret kv-v2 2>/dev/null || echo "Secret engine ya habilitado"

# Database secrets
echo "📦 Configurando secretos de base de datos..."
vault kv put secret/database \
  url='jdbc:postgresql://localhost:5432/mydb' \
  username='postgre' \
  password='C2wvVCP18#6@'

# JWT secrets
echo "🔑 Configurando secretos JWT..."
vault kv put secret/jwt \
  secret='your-256-bit-secret-key-change-this-in-production-make-it-long' \
  expiration='3600000' \
  refresh-secret='your-refresh-secret-key-also-change-this'

# SMTP secrets
echo "📧 Configurando secretos SMTP..."
vault kv put secret/smtp \
  host='smtp.gmail.com' \
  port='587' \
  username='your-email@gmail.com' \
  password='your-app-password'


# API Gateway secrets
echo "🚪 Configurando secretos API Gateway..."
vault kv put secret/api-gateway \
  api-key='your-api-gateway-key-12345' \
  rate-limit='1000'

# External APIs
echo "🌐 Configurando APIs externas..."
vault kv put secret/external-apis \
  openai-key='sk-proj-xxxxxxxxxxxx' \
  payment-gateway-key='pk_test_xxxxxxxxxxxx'

echo ""
echo "✅ Secretos inicializados correctamente"
echo ""

# Crear política de acceso para la aplicación
echo "📜 Creando políticas de acceso..."
vault policy write app-policy - <<EOF
path "secret/data/*" {
  capabilities = ["read", "list"]
}

path "secret/metadata/*" {
  capabilities = ["list"]
}
EOF

# Habilitar autenticación por AppRole
vault auth enable approle 2>/dev/null || echo "AppRole ya habilitado"

# Crear rol para la aplicación
echo "🎭 Configurando AppRole..."
vault write auth/approle/role/my-app \
  token_policies="app-policy" \
  token_ttl=1h \
  token_max_ttl=4h \
  bind_secret_id=true

# Obtener Role ID y Secret ID
echo ""
echo "🔐 Credenciales de AppRole (guárdalas para producción):"
echo "================================================"
vault read auth/approle/role/my-app/role-id
vault write -f auth/approle/role/my-app/secret-id
echo "================================================"
echo ""

echo "✅ Configuración de Vault completada"
echo "🌐 Vault UI disponible en: http://localhost:8200"
echo "🔑 Token de desarrollo: dev-root-token-id"