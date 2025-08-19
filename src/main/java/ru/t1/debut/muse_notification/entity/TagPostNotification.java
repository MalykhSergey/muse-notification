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
@Table(name = "tag_post_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagPostNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "tagName", nullable = false)
    private String tagName;

    public TagPostNotification(UUID userId, Long postId, String tagName) {
        this.userId = userId;
        this.postId = postId;
        this.tagName = tagName;
    }

    public static NotificationDTO toNotificationDTO(TagPostNotification tagPostNotification) {
        String message = "Новый пост (" + tagPostNotification.postId + ") для тэга: " + tagPostNotification.getTagName();
        return new NotificationDTO(tagPostNotification.getId(), EventType.NEW_POST_FOR_TAG, message);
    }
}
