package ru.t1.debut.muse_notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {
    private EventType eventType;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<UUID> usersUUID;
    private String description;
}
