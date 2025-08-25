package ru.t1.debut.muse_notification.service;

import ru.t1.debut.muse_notification.entity.AnswerNotification;
import ru.t1.debut.muse_notification.entity.CommentNotification;
import ru.t1.debut.muse_notification.entity.ModerNotification;
import ru.t1.debut.muse_notification.entity.TagPostNotification;

import java.util.List;

public interface NotificationService {

    void processCreateAnswerNotifications(List<AnswerNotification> notifications);

    void processCreateCommentNotifications(List<CommentNotification> notifications);

    void processCreatePostForTagNotifications(List<TagPostNotification> notifications);

    void processModerNotification(ModerNotification notification);
}
