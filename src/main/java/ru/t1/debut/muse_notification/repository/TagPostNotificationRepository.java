package ru.t1.debut.muse_notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.debut.muse_notification.entity.TagPostNotification;

@Repository
public interface TagPostNotificationRepository extends NotificationRepository<TagPostNotification> {
}