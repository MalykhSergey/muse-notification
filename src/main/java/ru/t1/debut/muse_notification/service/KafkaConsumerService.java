package ru.t1.debut.muse_notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.t1.debut.muse_notification.dto.*;
import ru.t1.debut.muse_notification.entity.AnswerNotification;
import ru.t1.debut.muse_notification.entity.CommentNotification;
import ru.t1.debut.muse_notification.entity.ModerNotification;
import ru.t1.debut.muse_notification.entity.TagPostNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final List<NotificationService> notificationServiceList;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.notifications-topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEventMessage(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition, @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            EventMessage baseEvent = objectMapper.readValue(message, EventMessage.class);

            switch (baseEvent.getEventType()) {
                case NEW_ANSWER_FOR_POST:
                case NEW_ANSWER_FOR_YOUR_POST: {
                    CreateAnswerEvent event = objectMapper.readValue(message, CreateAnswerEvent.class);
                    List<AnswerNotification> notifications = new ArrayList<>();
                    for (UUID userId : event.getUsersUUID()) {
                        try {
                            userService.ensureUser(userId);
                            boolean forAuthor = event.getEventType() == EventType.NEW_ANSWER_FOR_YOUR_POST;
                            AnswerNotification notification = new AnswerNotification(
                                    null,
                                    userId,
                                    event.getParentId(),
                                    event.getAnswerId(),
                                    forAuthor,
                                    event.getDescription()
                            );
                            notifications.add(notification);
                        } catch (RuntimeException runtimeException) {
                            log.error("Error processing message: {}", message, runtimeException);
                        }
                    }
                    notificationServiceList.forEach(service -> service.processCreateAnswerNotifications(notifications));
                    break;
                }
                case NEW_COMMENT_FOR_POST:
                case NEW_COMMENT_FOR_YOUR_POST: {
                    CreateCommentEvent commentEvent = objectMapper.readValue(message, CreateCommentEvent.class);
                    List<CommentNotification> notifications = new ArrayList<>();
                    for (UUID userId : commentEvent.getUsersUUID()) {
                        try {
                            userService.ensureUser(userId);
                            boolean forAuthor = commentEvent.getEventType() == EventType.NEW_COMMENT_FOR_YOUR_POST;
                            CommentNotification notification = new CommentNotification(
                                    null,
                                    userId,
                                    commentEvent.getPostId(),
                                    commentEvent.getCommentId(),
                                    forAuthor,
                                    commentEvent.getDescription()
                            );
                            notifications.add(notification);
                        } catch (RuntimeException runtimeException) {
                            log.error("Error processing message: {}", message, runtimeException);
                        }
                    }
                    notificationServiceList.forEach(service -> service.processCreateCommentNotifications(notifications));
                    break;
                }
                case NEW_POST_FOR_TAG: {
                    CreatePostForTag postForTagEvent = objectMapper.readValue(message, CreatePostForTag.class);
                    List<TagPostNotification> notifications = new ArrayList<>();
                    for (UUID userId : postForTagEvent.getUsersUUID()) {
                        try {
                            userService.ensureUser(userId);
                            TagPostNotification notification = new TagPostNotification(null, userId, postForTagEvent.getPostId(), postForTagEvent.getTagName(), postForTagEvent.getDescription());
                            notifications.add(notification);
                        } catch (RuntimeException runtimeException) {
                            log.error("Error processing message: {}", message, runtimeException);
                        }
                    }
                    notificationServiceList.forEach(service -> service.processCreatePostForTagNotifications(notifications));
                    break;
                }
                case MODERATOR_EDIT_YOUR_ANSWER:
                case MODERATOR_EDIT_YOUR_COMMENT:
                case MODERATOR_DELETE_YOUR_ANSWER:
                case MODERATOR_EDIT_YOUR_QUESTION:
                case MODERATOR_DELETE_YOUR_COMMENT:
                case MODERATOR_DELETE_YOUR_QUESTION:
                    ModeratorEvent moderatorEvent = objectMapper.readValue(message, ModeratorEvent.class);
                    for (UUID userId : moderatorEvent.getUsersUUID()) {
                        try {
                            userService.ensureUser(userId);
                            ModerNotification notification = new ModerNotification(null, userId, moderatorEvent.getEntityId(), moderatorEvent.getEventType(), moderatorEvent.getDescription());
                            notificationServiceList.forEach(service -> service.processModerNotification(notification));
                        } catch (RuntimeException runtimeException) {
                            log.error("Error processing message: {}", message, runtimeException);
                        }
                    }
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
