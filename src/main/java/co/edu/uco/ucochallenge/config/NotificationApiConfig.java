package co.edu.uco.ucochallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NotificationApiConfig {

    private final NotificationApiProperties properties;

    public NotificationApiConfig(NotificationApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestClient notificationApiRestClient() {
        return RestClient.builder()
            .baseUrl(properties.getBaseUrl())
            .defaultHeader("Authorization", "Basic " + encodeCredentials())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    private String encodeCredentials() {
        String credentials = properties.getClientId() + ":" + properties.getClientSecret();
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public String getClientId() {
        return properties.getClientId();
    }

    public String getClientSecret() {
        return properties.getClientSecret();
    }
}