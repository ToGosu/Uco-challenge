#!/bin/bash

export VAULT_ADDR='http://localhost:8200'
export VAULT_TOKEN='root'

echo "Inicializando Vault con secretos..."

sleep 5

vault secrets enable -path=secret kv-v2 2>/dev/null || echo "Secret engine ya habilitado"

echo "Configurando secretos de base de datos..."
vault kv put secret/database \
  url='jdbc:postgresql://localhost:5432/mydb' \
  username='postgres' \
  password='C2wvVCP18#6@'

echo "Configurando secretos JWT..."
vault kv put secret/jwt \
  secret='your-256-bit-secret-key-change-this-in-production-make-it-long' \
  expiration='3600000' \
  refresh-secret='your-refresh-secret-key-also-change-this'

echo "Configurando secretos SMTP..."
vault kv put secret/smtp \
  host='smtp.gmail.com' \
  port='587' \
  username='your-email@gmail.com' \
  password='your-app-password'

echo "Configurando secretos NotificationAPI..."
vault kv put secret/notificationapi \
  base-url='https://api.notificationapi.com' \
  client-id='71ys9hvevyqib7ecozhprd0sr1' \
  client-secret='6u2xwg1knmb07rvgttj8mj50e4ut41tjv6p7qhuxkreep0skkumb0eo902'

echo "Configurando secretos Auth0..."
vault kv put secret/auth0 \
  issuer-uri='https://dev-l7bs34cafn0six34.us.auth0.com/' \
  audience='https://ucochallenge-api/'

echo "Configurando secretos API Gateway..."
vault kv put secret/api-gateway \
  api-key='your-api-gateway-key-12345' \
  rate-limit='1000'

echo "Configurando APIs externas..."
vault kv put secret/external-apis \
  openai-key='sk-proj-xxxxxxxxxxxx' \
  payment-gateway-key='pk_test_xxxxxxxxxxxx'

echo ""
echo "Secretos inicializados correctamente"
echo ""

echo "Creando políticas de acceso..."
vault policy write app-policy - <<EOF
path "secret/data/*" {
  capabilities = ["read", "list"]
}

path "secret/metadata/*" {
  capabilities = ["list"]
}
EOF

vault auth enable approle 2>/dev/null || echo "AppRole ya habilitado"

echo "Configurando AppRole..."
vault write auth/approle/role/my-app \
  token_policies="app-policy" \
  token_ttl=1h \
  token_max_ttl=4h \
  bind_secret_id=true

echo ""
echo "Credenciales de AppRole (guárdalas para producción):"
echo "================================================"
vault read auth/approle/role/my-app/role-id
vault write -f auth/approle/role/my-app/secret-id
echo "================================================"
echo ""

echo "Configuración de Vault completada"
echo "Vault UI disponible en: http://localhost:8200"
echo "Token de desarrollo: root"