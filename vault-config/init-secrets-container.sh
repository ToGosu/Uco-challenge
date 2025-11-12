#!/bin/bash

export VAULT_ADDR="${VAULT_ADDR:-http://vault:8200}"
export VAULT_TOKEN="${VAULT_TOKEN:-root}"

echo "Inicializando Vault con secretos..."
echo "Vault URL: $VAULT_ADDR"

echo "Esperando a que Vault esté listo..."
for i in {1..30}; do
    if vault status &> /dev/null; then
        echo "Vault está disponible"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "Vault no está disponible después de 30 intentos"
        exit 1
    fi
    sleep 1
done

echo "Habilitando secrets engine..."
vault secrets enable -path=secret kv-v2 2>/dev/null || echo "   Secret engine ya habilitado"

echo "Configurando secretos de base de datos..."
vault kv put secret/database \
  url='jdbc:postgresql://db:5432/ucochallenge' \
  username='postgres' \
  password='C2wvVCP18#6@' 2>/dev/null || echo "   Secretos de BD ya configurados"

echo "Configurando secretos JWT..."
vault kv put secret/jwt \
  secret='your-256-bit-secret-key-change-this-in-production-make-it-long' \
  expiration='3600000' \
  refresh-secret='your-refresh-secret-key-also-change-this' 2>/dev/null || echo "   Secretos JWT ya configurados"

echo "Configurando secretos SMTP..."
vault kv put secret/smtp \
  host='smtp.gmail.com' \
  port='587' \
  username='your-email@gmail.com' \
  password='your-app-password' 2>/dev/null || echo "   Secretos SMTP ya configurados"

echo "Configurando secretos NotificationAPI..."
vault kv put secret/notificationapi \
  base-url='https://api.notificationapi.com' \
  client-id='71ys9hvevyqib7ecozhprd0sr1' \
  client-secret='6u2xwg1knmb07rvgttj8mj50e4ut41tjv6p7qhuxkreep0skkumb0eo902' 2>/dev/null || echo "   Secretos NotificationAPI ya configurados"

echo "Configurando secretos Auth0..."
vault kv put secret/auth0 \
  issuer-uri='https://dev-l7bs34cafn0six34.us.auth0.com/' \
  audience='https://ucochallenge-api/' 2>/dev/null || echo "   Secretos Auth0 ya configurados"

echo "Configurando secretos API Gateway..."
vault kv put secret/api-gateway \
  api-key='your-api-gateway-key-12345' \
  rate-limit='1000' 2>/dev/null || echo "   Secretos API Gateway ya configurados"

echo "Configurando APIs externas..."
vault kv put secret/external-apis \
  openai-key='sk-proj-xxxxxxxxxxxx' \
  payment-gateway-key='pk_test_xxxxxxxxxxxx' 2>/dev/null || echo "   Secretos de APIs externas ya configurados"

echo "Creando políticas de acceso..."
vault policy write app-policy - <<EOF 2>/dev/null || echo "   Política ya existe"
path "secret/data/*" {
  capabilities = ["read", "list"]
}

path "secret/metadata/*" {
  capabilities = ["list"]
}
EOF

vault auth enable approle 2>/dev/null || echo "   AppRole ya habilitado"

echo "Configurando AppRole..."
vault write auth/approle/role/my-app \
  token_policies="app-policy" \
  token_ttl=1h \
  token_max_ttl=4h \
  bind_secret_id=true 2>/dev/null || echo "   AppRole ya configurado"

echo ""
echo "Configuración de Vault completada"
echo "Vault UI disponible en: http://localhost:8200"
echo "Token de desarrollo: root"

