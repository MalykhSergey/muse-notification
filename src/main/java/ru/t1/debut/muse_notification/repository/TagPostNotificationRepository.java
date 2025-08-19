package ru.t1.debut.muse_notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.debut.muse_notification.entity.TagPostNotification;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagPostNotificationRepository extends JpaRepository<TagPostNotification, Long> {
    List<TagPostNotification> findAllByUserId(UUID id);

}