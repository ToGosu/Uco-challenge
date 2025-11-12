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
    private final String baseUrl;
    private final NotificationApiService notificationApiService;

    public NotificationClient(
            @Value("${notification.catalog.url}") String catalogServiceUrl,
            @Value("${notification.base-url:https://localhost:8443/uco-challenge}") String baseUrl,
            NotificationApiService notificationApiService) {
        this.catalogServiceUrl = catalogServiceUrl;
        this.baseUrl = baseUrl;
        this.notificationApiService = notificationApiService;
        this.restClient = RestClient.create();
    }

    public void sendWelcomeEmail(String email, String nombre) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);
            variables.put("email", email);

            boolean sent = notificationApiService.sendEmail(
                "welcome_email",
                UUID.randomUUID().toString(),
                email,
                variables
            );

            if (sent) {
                System.out.println("Email de bienvenida enviado exitosamente a: " + email);
            } else {
                System.err.println("No se pudo enviar el email a: " + email);
            }

        } catch (Exception e) {
            System.err.println("Error en el flujo de envío de email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendWelcomeSms(String phone, String nombre) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);

            boolean sent = notificationApiService.sendSms(
                "welcome_sms",
                UUID.randomUUID().toString(),
                phone,
                variables
            );

            if (sent) {
                System.out.println("SMS de bienvenida enviado exitosamente a: " + phone);
            } else {
                System.err.println("No se pudo enviar el SMS a: " + phone);
            }

        } catch (Exception e) {
            System.err.println("Error enviando SMS: " + e.getMessage());
        }
    }

    public void sendWelcomeEmailWithCatalog(String email, String nombre) {
        try {
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
            System.err.println("Error: " + e.getMessage());
            sendWelcomeEmail(email, nombre);
        }
    }

    public void sendEmailConfirmation(String email, String nombre, String code, String userId) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);
            variables.put("email", email);
            variables.put("code", code);
            variables.put("token", code);
            variables.put("subject", "Confirma tu correo electrónico - UCO Challenge");
            variables.put("html", buildEmailConfirmationHtml(nombre, code));

            boolean sent = notificationApiService.sendEmail(
                "email_confirmation",
                userId,
                email,
                variables
            );

            if (sent) {
                System.out.println("Email de confirmación enviado exitosamente a: " + email);
            } else {
                System.err.println("No se pudo enviar el email de confirmación a: " + email);
            }

        } catch (Exception e) {
            System.err.println("Error enviando email de confirmación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMobileConfirmation(String phone, String nombre, String code) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("nombre", nombre);
            variables.put("token", code);
            variables.put("code", code);
            variables.put("message", "Hola " + nombre + ", tu código de confirmación de celular es: " + code + ". Válido por 24 horas.");

            boolean sent = notificationApiService.sendSms(
                "mobile_confirmation",
                UUID.randomUUID().toString(),
                phone,
                variables
            );

            if (sent) {
                System.out.println("SMS de confirmación enviado exitosamente a: " + phone);
            } else {
                System.err.println("No se pudo enviar el SMS de confirmación a: " + phone);
            }

        } catch (Exception e) {
            System.err.println("Error enviando SMS de confirmación: " + e.getMessage());
        }
    }

    private String buildEmailConfirmationHtml(String nombre, String code) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 20px; background-color: #f9f9f9; }" +
               ".code-box { background-color: #ffffff; border: 2px solid #4CAF50; border-radius: 8px; padding: 20px; text-align: center; margin: 20px 0; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #4CAF50; }" +
               ".footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Bienvenido a UCO Challenge</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Hola " + nombre + "!</h2>" +
               "<p>Gracias por registrarte en UCO Challenge.</p>" +
               "<p>Para completar tu registro, por favor ingresa el siguiente código de confirmación en la aplicación:</p>" +
               "<div class='code-box'>" + code + "</div>" +
               "<p><strong>Nota:</strong> Este código expirará en 24 horas.</p>" +
               "<p>Si no creaste esta cuenta, puedes ignorar este mensaje.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>© 2024 UCO Challenge. Todos los derechos reservados.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}