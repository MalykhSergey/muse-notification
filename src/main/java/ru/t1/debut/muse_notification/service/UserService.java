package ru.t1.debut.muse_notification.service;

import ru.t1.debut.muse_notification.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User ensureUser(UUID id);
    Optional<User> getUser(UUID id);
}
