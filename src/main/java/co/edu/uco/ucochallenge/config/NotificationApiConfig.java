package co.edu.uco.ucochallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NotificationApiConfig {

    // ✅ USAR GUIONES en @Value (kebab-case)
    @Value("${notificationapi.base-url}")
    private String baseUrl;

    @Value("${notificationapi.client-id}")
    private String clientId;

    @Value("${notificationapi.client-secret}")
    private String clientSecret;

    @Bean
    public RestClient notificationApiRestClient() {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Basic " + encodeCredentials())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    private String encodeCredentials() {
        String credentials = clientId + ":" + clientSecret;
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}