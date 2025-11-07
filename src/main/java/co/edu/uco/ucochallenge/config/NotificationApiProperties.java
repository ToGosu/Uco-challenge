package co.edu.uco.ucochallenge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notificationapi")
@Data
public class NotificationApiProperties {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
}