package ru.t1.debut.muse_notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.t1.debut.muse_notification.dto.NotificationDTO;
import ru.t1.debut.muse_notification.service.DatabaseNotificationService;
import ru.t1.debut.muse_notification.service.WebSocketNotificationService;

import java.util.List;
import java.util.UUID;

@Controller
@MessageMapping("/notifications")
public class NotificationController {
    private final DatabaseNotificationService databaseNotificationService;
    private final WebSocketNotificationService webSocketNotificationService;

    @Autowired
    public NotificationController(DatabaseNotificationService databaseNotificationService, WebSocketNotificationService webSocketNotificationService) {
        this.databaseNotificationService = databaseNotificationService;
        this.webSocketNotificationService = webSocketNotificationService;
    }


    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        Authentication auth = (Authentication) event.getUser();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            List<NotificationDTO> allPendingNotifications = databaseNotificationService.getAllPendingNotifications(UUID.fromString(username));
            webSocketNotificationService.sendAllToUser(username, allPendingNotifications);
        }
    }
}
