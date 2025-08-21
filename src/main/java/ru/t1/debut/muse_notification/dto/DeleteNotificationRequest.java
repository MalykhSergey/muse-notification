package ru.t1.debut.muse_notification.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteNotificationRequest {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private EventType eventType;
}
