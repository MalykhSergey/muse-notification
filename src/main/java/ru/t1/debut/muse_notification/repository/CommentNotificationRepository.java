package ru.t1.debut.muse_notification.repository;

import org.springframework.stereotype.Repository;
import ru.t1.debut.muse_notification.entity.CommentNotification;

@Repository
public interface CommentNotificationRepository extends NotificationRepository<CommentNotification> {
}