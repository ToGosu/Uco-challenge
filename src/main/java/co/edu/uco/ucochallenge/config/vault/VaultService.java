package co.edu.uco.ucochallenge.config.vault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Optional;

@Service
public class VaultService {

    private static final Logger log = LoggerFactory.getLogger(VaultService.class);
    
    private final VaultTemplate vaultTemplate;

    @Autowired
    public VaultService(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public Optional<String> getSecret(String path, String key) {
        try {
            VaultResponse response = vaultTemplate.read(path);
            if (response != null && response.getData() != null) {
                Object value = response.getData().get(key);
                return Optional.ofNullable(value != null ? value.toString() : null);
            }
        } catch (Exception e) {
            log.error("Error obteniendo secreto de Vault: path={}, key={}", path, key, e);
        }
        return Optional.empty();
    }

    public Optional<Map<String, Object>> getAllSecrets(String path) {
        try {
            VaultResponse response = vaultTemplate.read(path);
            if (response != null && response.getData() != null) {
                return Optional.of(response.getData());
            }
        } catch (Exception e) {
            log.error("Error obteniendo secretos de Vault: path={}", path, e);
        }
        return Optional.empty();
    }

    public boolean writeSecret(String path, Map<String, Object> data) {
        try {
            vaultTemplate.write(path, data);
            log.info("Secreto escrito exitosamente en: {}", path);
            return true;
        } catch (Exception e) {
            log.error("Error escribiendo secreto en Vault: path={}", path, e);
            return false;
        }
    }

    public boolean deleteSecret(String path) {
        try {
            vaultTemplate.delete(path);
            log.info("Secreto eliminado exitosamente: {}", path);
            return true;
        } catch (Exception e) {
            log.error("Error eliminando secreto de Vault: path={}", path, e);
            return false;
        }
    }

    public boolean isVaultHealthy() {
        try {
            vaultTemplate.opsForSys().health();
            return true;
        } catch (Exception e) {
            log.error("Vault no está disponible", e);
            return false;
        }
    }
}