package ru.t1.debut.muse_notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                );
        return http.build();
    }


    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
                );

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    @Bean
    public RestTemplate keycloakRestTemplate(ClientRegistrationRepository clients, OAuth2AuthorizedClientManager authorizedClientManager) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("keycloak-admin")
                    .principal("keycloak-service")
                    .build();

            OAuth2AuthorizedClient client = authorizedClientManager.authorize(authorizeRequest);
            String token = client.getAccessToken().getTokenValue();

            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}