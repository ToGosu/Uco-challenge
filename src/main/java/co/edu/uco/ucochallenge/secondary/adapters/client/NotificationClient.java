package co.edu.uco.ucochallenge.secondary.adapters.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucochallenge.secondary.adapters.notification.NotificationApiService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class NotificationClient {

    private final RestClient restClient;
    private final String catalogServiceUrl;
    private final NotificationApiService notificationApiService;

    public NotificationClient(
            @Value("${notification.catalog.url}") String catalogServiceUrl,
            NotificationApiService notificationApiService) {
        this.catalogServiceUrl = catalogServiceUrl;
        this.notificationApiService = notificationApiService;
        this.restClient = RestClient.create();
    }

    /**
     * Envía email de bienvenida usando:
     * 1. Template del catálogo (parameters-service) - OPCIONAL
     * 2. NotificationAPI para envío real
     */
    public void sendWelcomeEmail(String email, String nombre) {
        try {
            // Variables para el template
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);
            variables.put("email", email);

            // Enviar con NotificationAPI
            // El template "welcome_email" debe estar configurado en NotificationAPI dashboard
            boolean sent = notificationApiService.sendEmail(
                "welcome_email",           // ID del template en NotificationAPI
                UUID.randomUUID().toString(), // User ID único
                email,
                variables
            );

            if (sent) {
                System.out.println("✅ Email de bienvenida enviado exitosamente a: " + email);
            } else {
                System.err.println("⚠️ No se pudo enviar el email a: " + email);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error en el flujo de envío de email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envía SMS de bienvenida usando NotificationAPI
     */
    public void sendWelcomeSms(String phone, String nombre) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);

            // Enviar con NotificationAPI
            boolean sent = notificationApiService.sendSms(
                "welcome_sms",                // ID del template en NotificationAPI
                UUID.randomUUID().toString(), // User ID único
                phone,
                variables
            );

            if (sent) {
                System.out.println("✅ SMS de bienvenida enviado exitosamente a: " + phone);
            } else {
                System.err.println("⚠️ No se pudo enviar el SMS a: " + phone);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error enviando SMS: " + e.getMessage());
        }
    }

    /**
     * Versión avanzada: Usa template del catálogo + NotificationAPI
     */
    public void sendWelcomeEmailWithCatalog(String email, String nombre) {
        try {
            // 1️⃣ Obtener template del catálogo local
            Map<String, Object> templateRequest = Map.of(
                "templateCode", "WELCOME_EMAIL",
                "variables", Map.of(
                    "nombre", nombre,
                    "email", email
                )
            );

            Map<?, ?> templateResponse = restClient.post()
                .uri(catalogServiceUrl + "/api/v1/notifications/process")
                .contentType(MediaType.APPLICATION_JSON)
                .body(templateRequest)
                .retrieve()
                .body(Map.class);

            // 2️⃣ Enviar usando NotificationAPI con el contenido procesado
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);
            variables.put("email", email);
            
            if (templateResponse != null && templateResponse.get("processedBody") != null) {
                variables.put("customBody", (String) templateResponse.get("processedBody"));
            }

            notificationApiService.sendEmail(
                "welcome_email",
                UUID.randomUUID().toString(),
                email,
                variables
            );

        } catch (Exception e) {
            System.err.println("⚠️ Error: " + e.getMessage());
            // Fallback: usar solo NotificationAPI
            sendWelcomeEmail(email, nombre);
        }
    }
}