package co.edu.uco.ucochallenge.config.vault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class VaultService {

    private final VaultTemplate vaultTemplate;

    @Autowired
    public VaultService(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    /**
     * Obtiene un secreto específico de Vault
     * 
     * @param path Ruta del secreto (ej: "secret/data/database")
     * @param key  Clave del secreto (ej: "password")
     * @return Valor del secreto o Optional.empty() si no existe
     */
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

    /**
     * Obtiene todos los secretos de un path
     * 
     * @param path Ruta completa (ej: "secret/data/database")
     * @return Map con todos los secretos o Optional.empty()
     */
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

    /**
     * Escribe un secreto en Vault
     * 
     * @param path Ruta donde escribir (ej: "secret/data/mi-secreto")
     * @param data Map con los datos a guardar
     * @return true si se escribió correctamente
     */
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

    /**
     * Elimina un secreto de Vault
     */
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

    /**
     * Verifica si Vault está disponible y funcionando
     */
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