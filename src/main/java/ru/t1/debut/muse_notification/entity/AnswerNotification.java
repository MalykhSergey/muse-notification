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
@Table(name = "answer_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnswerNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "for_author", nullable = false)
    private Boolean forAuthor;

    public AnswerNotification(UUID userId, Long parentId, Long answerId, Boolean forAuthor) {
        this.userId = userId;
        this.parentId = parentId;
        this.answerId = answerId;
        this.forAuthor = forAuthor;
    }

    public static NotificationDTO toNotificationDTO(AnswerNotification answerNotification) {
        String message = "Новый ответ (" + answerNotification.getAnswerId() + ") для поста: " + answerNotification.getParentId();
        return new NotificationDTO(answerNotification.getId(), EventType.NEW_ANSWER_FOR_POST, message);
    }
}