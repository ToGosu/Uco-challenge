#!/bin/bash

set -e

CERT_DIR="certs"
CA_KEY="$CERT_DIR/ca.key"
CA_CRT="$CERT_DIR/ca.crt"
VALIDITY_DAYS=365

echo "Generando certificados SSL autofirmados para desarrollo..."

mkdir -p "$CERT_DIR"

if [ ! -f "$CA_KEY" ]; then
    echo "Generando clave privada de CA..."
    openssl genrsa -out "$CA_KEY" 4096
fi

if [ ! -f "$CA_CRT" ]; then
    echo "Generando certificado CA..."
    openssl req -new -x509 -days $VALIDITY_DAYS -key "$CA_KEY" -out "$CA_CRT" \
        -subj "//C=CO/ST=Antioquia/L=Medellin/O=UCO Challenge/OU=Development/CN=UCO Challenge CA"
fi

generate_cert() {
    local SERVICE_NAME=$1
    local SERVICE_KEY="$CERT_DIR/$SERVICE_NAME.key"
    local SERVICE_CSR="$CERT_DIR/$SERVICE_NAME.csr"
    local SERVICE_CRT="$CERT_DIR/$SERVICE_NAME.crt"
    local SAN="DNS:localhost,DNS:$SERVICE_NAME,IP:127.0.0.1"

    echo "Generando certificado para $SERVICE_NAME..."

    openssl genrsa -out "$SERVICE_KEY" 2048

    openssl req -new -key "$SERVICE_KEY" -out "$SERVICE_CSR" \
        -subj "//C=CO/ST=Antioquia/L=Medellin/O=UCO Challenge/OU=Development/CN=$SERVICE_NAME"

    cat > "$CERT_DIR/$SERVICE_NAME.ext" <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
DNS.2 = $SERVICE_NAME
IP.1 = 127.0.0.1
EOF

    openssl x509 -req -in "$SERVICE_CSR" -CA "$CA_CRT" -CAkey "$CA_KEY" \
        -CAcreateserial -out "$SERVICE_CRT" -days $VALIDITY_DAYS \
        -extfile "$CERT_DIR/$SERVICE_NAME.ext"

    rm "$SERVICE_CSR" "$CERT_DIR/$SERVICE_NAME.ext"

    echo "Certificado generado para $SERVICE_NAME"
}

generate_cert "backend"
generate_cert "api-gateway"
generate_cert "catalog-service"
generate_cert "nginx-waf"

echo "Generando certificado combinado para Nginx..."
cat "$CERT_DIR/nginx-waf.crt" "$CERT_DIR/nginx-waf.key" > "$CERT_DIR/nginx-waf.pem" 2>/dev/null || true

echo ""
echo "Todos los certificados han sido generados en el directorio $CERT_DIR/"
echo ""
echo "Archivos generados:"
echo "   - CA: $CA_CRT, $CA_KEY"
echo "   - Backend: $CERT_DIR/backend.crt, $CERT_DIR/backend.key"
echo "   - API Gateway: $CERT_DIR/api-gateway.crt, $CERT_DIR/api-gateway.key"
echo "   - Catalog Service: $CERT_DIR/catalog-service.crt, $CERT_DIR/catalog-service.key"
echo "   - Nginx WAF: $CERT_DIR/nginx-waf.crt, $CERT_DIR/nginx-waf.key"
echo ""
echo "IMPORTANTE: Estos son certificados autofirmados solo para desarrollo."
echo "   Para producción, use certificados de una CA confiable (Let's Encrypt, etc.)"
echo ""

