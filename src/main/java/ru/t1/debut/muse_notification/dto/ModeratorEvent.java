package ru.t1.debut.muse_notification.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ModeratorEvent extends EventMessage {
    private Long entityId;
}
