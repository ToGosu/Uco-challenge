package co.edu.uco.ucochallenge.primary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uco.ucochallenge.secondary.adapters.client.NotificationClient;

import java.util.Map;

@RestController
@RequestMapping("/uco-challenge/api/v1/test")
public class NotificationTestController {

    private final NotificationClient notificationClient;

    public NotificationTestController(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    /**
     * POST /test/send-email
     * Body: {
     *   "email": "test@example.com",
     *   "nombre": "Juan"
     * }
     */
    @PostMapping("/send-email")
    public ResponseEntity<Map<String, String>> testEmail(@RequestBody Map<String, String> request) {
        try {
            notificationClient.sendWelcomeEmail(
                request.get("email"),
                request.get("nombre")
            );
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Email enviado correctamente via NotificationAPI"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * POST /test/send-sms
     * Body: {
     *   "phone": "+573001234567",
     *   "nombre": "Juan"
     * }
     */
    @PostMapping("/send-sms")
    public ResponseEntity<Map<String, String>> testSms(@RequestBody Map<String, String> request) {
        try {
            notificationClient.sendWelcomeSms(
                request.get("phone"),
                request.get("nombre")
            );
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "SMS enviado correctamente via NotificationAPI"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }
}
