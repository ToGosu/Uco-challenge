package co.edu.uco.ucochallenge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Auth0Properties auth0Properties;
    private final ApplicationContext applicationContext;

    public SecurityConfig(Auth0Properties auth0Properties, ApplicationContext applicationContext) {
        this.auth0Properties = auth0Properties;
        this.applicationContext = applicationContext;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/uco-challenge/api/v1/users/**").authenticated()
                .requestMatchers("/api/vault/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/health/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/test/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/cities/**", "/api/v1/cities/**", "/api/api/v1/cities/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/idtypes/**", "/api/v1/idtypes/**", "/api/api/v1/idtypes/**").permitAll()
                .anyRequest().permitAll()
            );
        
        // Solo configurar JWT si el issuer-uri está disponible y el bean existe
        String issuerUri = auth0Properties.getIssuerUri();
        if (StringUtils.hasText(issuerUri)) {
            try {
                JwtDecoder jwtDecoder = applicationContext.getBean(JwtDecoder.class);
                http.oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.decoder(jwtDecoder))
                );
            } catch (Exception e) {
                // Si el bean no existe, no configuramos OAuth2 Resource Server
                // Esto puede pasar si la propiedad no está disponible
            }
        }
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "https://localhost:5173",
            "http://localhost:3000",
            "https://localhost:3000",
            "http://localhost:8080",
            "https://localhost:8443",
            "http://localhost:8090",
            "https://localhost:8090"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnProperty(name = "auth0.issuer-uri", matchIfMissing = false)
    public JwtDecoder jwtDecoder() {
        String issuerUri = auth0Properties.getIssuerUri();
        if (!StringUtils.hasText(issuerUri)) {
            throw new IllegalStateException("auth0.issuer-uri no puede estar vacío cuando JWT está habilitado");
        }
        
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        String audience = auth0Properties.getAudience();
        if (StringUtils.hasText(audience)) {
            OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
            OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
            OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
            jwtDecoder.setJwtValidator(withAudience);
        }

        return jwtDecoder;
    }

    /**
     * Validador personalizado de audience para Auth0
     */
    static class AudienceValidator implements OAuth2TokenValidator<Jwt> {
        private final String audience;

        AudienceValidator(String audience) {
            this.audience = audience;
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            if (jwt.getAudience().contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }
            OAuth2Error error = new OAuth2Error(
                "invalid_token", 
                "The required audience is missing", 
                null
            );
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}