# Vault Setup Instructions

## Problem Fixed

The application was failing to start with the error:
```
issuer cannot be empty
Status 403 Forbidden [secret/jwt]: permission denied
```

This was caused by an incorrect Vault token configuration.

## Changes Made

1. Updated `application.yml` - Changed Vault token from `dev-root-token-id` to `root`
2. Updated `docker-compose.yml` - Changed `VAULT_DEV_ROOT_TOKEN_ID` to `root`
3. Updated `vault-config/init-secrets.sh` - Changed Vault token to `root`

## Setup Instructions

### Step 1: Initialize Vault with Secrets

After starting Vault, you need to populate it with the required secrets. Run the initialization script:

```bash
cd vault-config
./init-secrets.sh
```

This script will populate Vault with:
- Database credentials (`secret/database`)
- JWT configuration (`secret/jwt`) - **This fixes the "issuer cannot be empty" error**
- SMTP settings (`secret/smtp`)
- NotificationAPI credentials (`secret/notificationapi`)
- Auth0 configuration (`secret/auth0`)
- API Gateway settings (`secret/api-gateway`)

### Step 2: Verify Vault is Running

Check that Vault is accessible:
```bash
curl http://localhost:8200/v1/sys/health
```

### Step 3: Start the Application

After populating Vault with secrets, start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

Or if using Docker:
```bash
docker-compose up
```

## Vault Access

- **Vault UI**: http://localhost:8200
- **Dev Token**: `root`

## Troubleshooting

If you still see "permission denied" errors:
1. Verify Vault is running and accessible
2. Ensure the init-secrets.sh script ran successfully
3. Check that the Vault token in application.yml matches the one in your Vault instance
