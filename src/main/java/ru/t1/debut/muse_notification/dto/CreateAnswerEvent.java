package ru.t1.debut.muse_notification.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateAnswerEvent extends EventMessage {
    private Long parentId;
    private Long answerId;

    public CreateAnswerEvent(EventType eventType, Set<UUID> usersUUID, Long parentId, Long answerId) {
        super(eventType, usersUUID);
        this.parentId = parentId;
        this.answerId = answerId;
    }
}
