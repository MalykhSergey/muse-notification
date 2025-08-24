package ru.t1.debut.muse_notification.dto;

import lombok.Data;

@Data
public class KeycloakUser {
    private String id;
    private String username;
    private String email;
}