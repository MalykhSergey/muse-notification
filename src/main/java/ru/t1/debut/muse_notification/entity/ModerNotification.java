package ru.t1.debut.muse_notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.t1.debut.muse_notification.dto.EventType;
import ru.t1.debut.muse_notification.dto.NotificationDTO;

import java.util.UUID;

@Entity
@Table(name = "moder_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModerNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    @Column
    private String description;

    public NotificationDTO toNotificationDTO() {
        return new NotificationDTO(this.getId(), eventType, this);
    }
}