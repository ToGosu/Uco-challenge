package co.edu.uco.ucochallenge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;

@Configuration
@VaultPropertySource(value = "secret/database", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/jwt", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/smtp", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/sms", renewal = VaultPropertySource.Renewal.ROTATE)
@VaultPropertySource(value = "secret/api-gateway", renewal = VaultPropertySource.Renewal.ROTATE)
public class VaultConfig {
    // Spring Cloud Vault se encarga automáticamente de leer estos secretos
    // y ponerlos disponibles como propiedades de configuración
}
