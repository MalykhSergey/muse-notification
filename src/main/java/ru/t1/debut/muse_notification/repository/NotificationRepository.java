package ru.t1.debut.muse_notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository<T> extends JpaRepository<T, Long> {
    List<T> findAllByUserId(UUID id);
    void deleteByIdAndUserId(Long id, UUID userId);
    void deleteByUserId(UUID userId);
}
