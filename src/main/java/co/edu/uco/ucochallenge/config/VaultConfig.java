package co.edu.uco.ucochallenge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.vault.enabled", havingValue = "true", matchIfMissing = false)
@VaultPropertySource(value = "secret/database", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/jwt", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/smtp", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/notificationapi", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/auth0", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/api-gateway", renewal = VaultPropertySource.Renewal.ROTATE)
public class VaultConfig {
    // Spring Cloud Vault se encarga automáticamente de leer estos secretos
    // y ponerlos disponibles como propiedades de configuración
    // Esta configuración solo se activa cuando spring.cloud.vault.enabled=true
}