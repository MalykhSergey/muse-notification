package ru.t1.debut.muse_notification.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.t1.debut.muse_notification.dto.EventType;
import ru.t1.debut.muse_notification.dto.NotificationDTO;
import ru.t1.debut.muse_notification.entity.AnswerNotification;
import ru.t1.debut.muse_notification.entity.CommentNotification;
import ru.t1.debut.muse_notification.entity.TagPostNotification;
import ru.t1.debut.muse_notification.repository.AnswerNotificationRepository;
import ru.t1.debut.muse_notification.repository.CommentNotificationRepository;
import ru.t1.debut.muse_notification.repository.TagPostNotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Order(1)
public class DatabaseNotificationService implements NotificationService {

    private final AnswerNotificationRepository answerNotificationRepository;
    private final CommentNotificationRepository commentNotificationRepository;
    private final TagPostNotificationRepository tagPostNotificationRepository;

    @Autowired
    public DatabaseNotificationService(AnswerNotificationRepository answerNotificationRepository, CommentNotificationRepository commentNotificationRepository, TagPostNotificationRepository tagPostNotificationRepository) {
        this.answerNotificationRepository = answerNotificationRepository;
        this.commentNotificationRepository = commentNotificationRepository;
        this.tagPostNotificationRepository = tagPostNotificationRepository;
    }

    public void processCreateAnswerNotifications(List<AnswerNotification> notifications) {
        answerNotificationRepository.saveAll(notifications);
    }

    public void processCreateCommentNotifications(List<CommentNotification> notifications) {
        commentNotificationRepository.saveAll(notifications);
    }

    public void processCreatePostForTagNotifications(List<TagPostNotification> notifications) {
        tagPostNotificationRepository.saveAll(notifications);
    }

    public List<NotificationDTO> getAnswerEvents(UUID userId) {
        return answerNotificationRepository.findAllByUserId(userId).stream().map(AnswerNotification::toNotificationDTO).toList();
    }

    public List<NotificationDTO> getCommentEvents(UUID userId) {
        return commentNotificationRepository.findAllByUserId(userId).stream().map(CommentNotification::toNotificationDTO).toList();
    }

    public List<NotificationDTO> getPostForTagEvents(UUID userId) {
        return tagPostNotificationRepository.findAllByUserId(userId).stream().map(TagPostNotification::toNotificationDTO).toList();
    }

    public List<NotificationDTO> getAllPendingNotifications(UUID userId) {
        List<NotificationDTO> allNotifications = new ArrayList<>();
        allNotifications.addAll(getAnswerEvents(userId));
        allNotifications.addAll(getCommentEvents(userId));
        allNotifications.addAll(getPostForTagEvents(userId));
        return allNotifications;
    }

    @Transactional
    public void deleteNotification(Long id, UUID userId, EventType eventType) {
        switch (eventType) {
            case NEW_ANSWER_FOR_POST:
            case NEW_ANSWER_FOR_YOUR_POST: {
                answerNotificationRepository.deleteByIdAndUserId(id, userId);
                break;
            }
            case NEW_POST_FOR_TAG: {
                tagPostNotificationRepository.deleteByIdAndUserId(id, userId);
                break;
            }
            case NEW_COMMENT_FOR_POST:
            case NEW_COMMENT_FOR_YOUR_POST: {
                commentNotificationRepository.deleteByIdAndUserId(id, userId);
                break;
            }
        }
    }

    @Transactional
    public void deleteAll(UUID userId) {
        answerNotificationRepository.deleteByUserId(userId);
        commentNotificationRepository.deleteByUserId(userId);
        tagPostNotificationRepository.deleteByUserId(userId);
    }
}
