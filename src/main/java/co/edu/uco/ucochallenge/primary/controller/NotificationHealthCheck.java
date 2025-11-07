package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.config.NotificationApiConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/uco-challenge/api/v1/health")
public class NotificationHealthCheck {

    private final NotificationApiConfig config;

    public NotificationHealthCheck(NotificationApiConfig config) {
        this.config = config;
    }

    @GetMapping("/notifications")
    public ResponseEntity<Map<String, Object>> checkNotificationServices() {
        Map<String, Object> status = new HashMap<>();

        // Verificar que las credenciales estén configuradas
        boolean credentialsConfigured = 
            config.getClientId() != null && !config.getClientId().isEmpty() &&
            config.getClientSecret() != null && !config.getClientSecret().isEmpty();

        status.put("notificationapi", Map.of(
            "status", credentialsConfigured ? "CONFIGURED" : "NOT_CONFIGURED",
            "service", "NotificationAPI",
            "clientId", config.getClientId().substring(0, 8) + "..."
        ));

        return ResponseEntity.ok(Map.of(
            "overall", credentialsConfigured ? "UP" : "DOWN",
            "services", status
        ));
    }
}
