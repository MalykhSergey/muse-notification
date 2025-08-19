package ru.t1.debut.muse_notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private EventType eventType;
    private String message;
}
