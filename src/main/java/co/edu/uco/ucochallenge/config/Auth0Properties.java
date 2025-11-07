package co.edu.uco.ucochallenge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth0")
@Data
public class Auth0Properties {
    private String issuerUri;
    private String audience;
}