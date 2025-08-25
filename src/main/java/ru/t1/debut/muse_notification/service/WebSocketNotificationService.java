package ru.t1.debut.muse_notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.t1.debut.muse_notification.dto.NotificationDTO;
import ru.t1.debut.muse_notification.entity.AnswerNotification;
import ru.t1.debut.muse_notification.entity.CommentNotification;
import ru.t1.debut.muse_notification.entity.ModerNotification;
import ru.t1.debut.muse_notification.entity.TagPostNotification;

import java.util.List;

@Service
@Order(2)
public class WebSocketNotificationService implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void processCreateAnswerNotifications(List<AnswerNotification> notifications) {
        notifications.forEach(notification -> messagingTemplate.convertAndSendToUser(notification.getUserId().toString(), "/queue/notifications", notification.toNotificationDTO()));
    }

    @Override
    public void processCreateCommentNotifications(List<CommentNotification> notifications) {
        notifications.forEach(notification -> messagingTemplate.convertAndSendToUser(notification.getUserId().toString(), "/queue/notifications", notification.toNotificationDTO()));
    }

    @Override
    public void processCreatePostForTagNotifications(List<TagPostNotification> notifications) {
        notifications.forEach(notification -> messagingTemplate.convertAndSendToUser(notification.getUserId().toString(), "/queue/notifications", notification.toNotificationDTO()));
    }

    @Override
    public void processModerNotification(ModerNotification notification) {
        messagingTemplate.convertAndSendToUser(notification.getUserId().toString(), "/queue/notifications", notification.toNotificationDTO());
    }

    public void sendAllToUser(String userName, List<NotificationDTO> notifications) {
        messagingTemplate.convertAndSendToUser(userName, "/queue/notifications", notifications);
    }
}
