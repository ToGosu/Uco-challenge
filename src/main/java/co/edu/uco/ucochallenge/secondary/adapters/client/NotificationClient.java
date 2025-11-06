package co.edu.uco.ucochallenge.secondary.adapters.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient() {
        this.restClient = RestClient.create("http://localhost:8082/api/v1/notifications");
    }

    public void sendWelcomeEmail(String email, String nombre) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("nombre", nombre);

        restClient.post()
                .uri("/send-welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Correo de bienvenida enviado a " + email);
    }

    public void sendWelcomeSms(String phone, String nombre) {
        Map<String, String> request = new HashMap<>();
        request.put("phone", phone);
        request.put("nombre", nombre);

        restClient.post()
                .uri("/send-sms")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        System.out.println("SMS de bienvenida enviado a " + phone);
    }
}
