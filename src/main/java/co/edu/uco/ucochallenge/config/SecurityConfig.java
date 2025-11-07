package co.edu.uco.ucochallenge.config;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Auth0Properties auth0Properties;

    public SecurityConfig(Auth0Properties auth0Properties) {
        this.auth0Properties = auth0Properties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/uco-challenge/api/v1/users/**").authenticated()
                .requestMatchers("/api/vault/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/health/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/test/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/cities/**").permitAll()
                .requestMatchers("/uco-challenge/api/v1/idtypes/**").permitAll()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(auth0Properties.getIssuerUri());

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(auth0Properties.getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(auth0Properties.getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);
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