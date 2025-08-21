package ru.t1.debut.muse_notification.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.debut.muse_notification.entity.AnswerNotification;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerNotificationRepository extends JpaRepository<AnswerNotification, Long> {
    List<AnswerNotification> findAllByUserId(UUID id);

    void deleteByIdAndUserId(Long id, UUID userId);
}