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
@Table(name = "comment_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "for_author", nullable = false)
    private Boolean forAuthor;

    @Column
    private String description;

    public NotificationDTO toNotificationDTO() {
        EventType eventType = this.forAuthor ? EventType.NEW_COMMENT_FOR_YOUR_POST : EventType.NEW_COMMENT_FOR_POST;
        return new NotificationDTO(this.getId(), eventType, this);
    }
}