package ru.t1.debut.muse_notification.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.t1.debut.muse_notification.dto.DeleteNotificationRequest;
import ru.t1.debut.muse_notification.dto.NotificationDTO;
import ru.t1.debut.muse_notification.service.DatabaseNotificationService;
import ru.t1.debut.muse_notification.service.WebSocketNotificationService;

import java.security.Principal;
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

    @MessageMapping("/delete")
    public void deleteNotification(@Valid DeleteNotificationRequest request, Principal principal) {
        if (principal != null) {
            UUID userId = UUID.fromString(principal.getName());
            databaseNotificationService.deleteNotification(request.getId(), userId, request.getEventType());
        }
    }

    @MessageMapping("/deleteAll")
    public void deleteNotification(Principal principal) {
        if (principal != null) {
            UUID userId = UUID.fromString(principal.getName());
            databaseNotificationService.deleteAll(userId);
        }
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
