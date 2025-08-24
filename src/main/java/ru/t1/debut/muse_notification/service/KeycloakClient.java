package ru.t1.debut.muse_notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.t1.debut.muse_notification.dto.KeycloakUser;

@Service
@RequiredArgsConstructor
public class KeycloakClient {

    private final RestTemplate keycloakRestTemplate;

    @Value("${spring.security.oauth2.url}")
    private String keycloakUrl;
    @Value("${spring.security.oauth2.client.registration.keycloak-admin.realm}")
    private String realm;


    public KeycloakUser getUserById(String userId) {
        String url = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
        try {
            ResponseEntity<KeycloakUser> response =
                    keycloakRestTemplate.exchange(url, HttpMethod.GET, null, KeycloakUser.class);
            return response.getBody();
        } catch (RestClientException restClientException) {
            throw new RuntimeException("User not found in auth server: " + userId);
        }
    }
}