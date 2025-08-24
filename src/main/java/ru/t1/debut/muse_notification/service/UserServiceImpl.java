package ru.t1.debut.muse_notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.debut.muse_notification.dto.KeycloakUser;
import ru.t1.debut.muse_notification.entity.User;
import ru.t1.debut.muse_notification.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakClient keycloakClient;

    @Override
    public User ensureUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            KeycloakUser keycloakUser = keycloakClient.getUserById(id.toString());
            User user = new User(UUID.fromString(keycloakUser.getId()), keycloakUser.getEmail(), keycloakUser.getUsername());
            return userRepository.save(user);
        }
        return optionalUser.get();
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id);
    }
}
