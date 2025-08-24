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
                                    userId,
                                    event.getParentId(),
                                    event.getAnswerId(),
                                    forAuthor
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
                                    userId,
                                    commentEvent.getPostId(),
                                    commentEvent.getCommentId(),
                                    forAuthor
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
                            TagPostNotification notification = new TagPostNotification(userId, postForTagEvent.getPostId(), postForTagEvent.getTagName());
                            notifications.add(notification);
                        } catch (RuntimeException runtimeException) {
                            log.error("Error processing message: {}", message, runtimeException);
                        }
                    }
                    notificationServiceList.forEach(service -> service.processCreatePostForTagNotifications(notifications));
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
