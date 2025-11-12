package co.edu.uco.ucochallenge.config;

import java.util.Arrays;

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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
            // CORS debe configurarse primero
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permitir endpoints de confirmación sin autenticación (deben ir ANTES de la regla general)
                .requestMatchers("/uco-challenge/api/v1/users/confirm-email", "/uco-challenge/v1/users/confirm-email").permitAll()
                .requestMatchers("/uco-challenge/api/v1/users/confirm-mobile", "/uco-challenge/v1/users/confirm-mobile").permitAll()
                // Permitir registro de usuarios sin autenticación
                .requestMatchers("/uco-challenge/api/v1/users", "/uco-challenge/v1/users").permitAll()
                // Los demás endpoints de usuarios requieren autenticación
                .requestMatchers("/uco-challenge/api/v1/users/**", "/uco-challenge/v1/users/**").authenticated()
                .requestMatchers("/api/vault/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/health/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/test/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/cities/**", "/api/v1/cities/**", "/api/api/v1/cities/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/idtypes/**", "/api/v1/idtypes/**", "/api/api/v1/idtypes/**").permitAll()
                .anyRequest().permitAll()
            );
        
        String issuerUri = auth0Properties.getIssuerUri();
        if (StringUtils.hasText(issuerUri)) {
            try {
                JwtDecoder jwtDecoder = applicationContext.getBean(JwtDecoder.class);
                http.oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.decoder(jwtDecoder))
                );
            } catch (Exception e) {
                // Si no se puede crear el JwtDecoder, continuar sin OAuth2
            }
        }
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Configurar orígenes permitidos
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5174",
            "https://localhost:5173",
            "https://localhost:5174",
            "http://localhost:3000",
            "https://localhost:3000",
            "http://localhost:8080",
            "https://localhost:8443",
            "http://localhost:8090",
            "https://localhost:8090"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Permitir todos los headers para evitar problemas
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setMaxAge(3600L); // Cache preflight por 1 hora
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Registrar CORS solo una vez para todas las rutas
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