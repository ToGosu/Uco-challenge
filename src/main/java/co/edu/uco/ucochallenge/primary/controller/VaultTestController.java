package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.config.DatabaseProperties;
import co.edu.uco.ucochallenge.config.JwtProperties;
import co.edu.uco.ucochallenge.config.SmtpProperties;
import co.edu.uco.ucochallenge.config.vault.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class VaultTestController {

    private final VaultService vaultService;
    private final DatabaseProperties databaseProperties;
    private final JwtProperties jwtProperties;
    private final SmtpProperties smtpProperties;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkVaultHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("vaultHealthy", vaultService.isVaultHealthy());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config/database")
    public ResponseEntity<Map<String, Object>> getDatabaseConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("url", databaseProperties.getUrl());
        config.put("username", databaseProperties.getUsername());
        config.put("passwordConfigured", databaseProperties.getPassword() != null);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/config/jwt")
    public ResponseEntity<Map<String, Object>> getJwtConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("secretConfigured", jwtProperties.getSecret() != null);
        config.put("expiration", jwtProperties.getExpiration());
        return ResponseEntity.ok(config);
    }

    @GetMapping("/config/smtp")
    public ResponseEntity<Map<String, Object>> getSmtpConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("host", smtpProperties.getHost());
        config.put("port", smtpProperties.getPort());
        config.put("username", smtpProperties.getUsername());
        config.put("passwordConfigured", smtpProperties.getPassword() != null);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/test-direct")
    public ResponseEntity<Map<String, Object>> testDirectAccess() {
        Map<String, Object> response = new HashMap<>();
        
        // Probar lectura directa desde Vault
        vaultService.getSecret("secret/data/database", "url")
            .ifPresent(url -> response.put("databaseUrl", url));
        
        vaultService.getSecret("secret/data/jwt", "secret")
            .ifPresent(secret -> response.put("jwtSecretConfigured", true));
        
        return ResponseEntity.ok(response);
    }
}
