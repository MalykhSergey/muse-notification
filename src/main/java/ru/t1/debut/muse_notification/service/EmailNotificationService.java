package ru.t1.debut.muse_notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.t1.debut.muse_notification.entity.AnswerNotification;
import ru.t1.debut.muse_notification.entity.CommentNotification;
import ru.t1.debut.muse_notification.entity.TagPostNotification;
import ru.t1.debut.muse_notification.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;
    @Value("${server.site_url}")
    private String siteUrl = "https://muse.innoca.local";

    @Override
    public void processCreateAnswerNotifications(List<AnswerNotification> notifications) {
        for (AnswerNotification notification : notifications) {
            Optional<User> user = userService.getUser(notification.getUserId());
            if (user.isEmpty()) {
                log.error("Send email error to: {}. User not exists!", notification.getUserId());
                continue;
            }
            String to = user.get().getEmail();
            String answerLink = buildAnswerLink(notification.getParentId(), notification.getAnswerId());

            String subject;
            String recipientText;

            if (notification.getForAuthor()) {
                subject = "Новый ответ на ваш вопрос";
                recipientText = "Пользователь ответил на ваш вопрос.";
            } else {
                subject = "Новый ответ в обсуждении, на которое вы подписаны";
                recipientText = "Появился новый ответ в обсуждении, на которое вы подписаны.";
            }

            sendEmail(to, subject, "email/answer-notification", Map.of(
                    "answerLink", answerLink,
                    "recipientText", recipientText
            ));
        }
    }

    @Override
    public void processCreateCommentNotifications(List<CommentNotification> notifications) {
        for (CommentNotification notification : notifications) {
            Optional<User> user = userService.getUser(notification.getUserId());
            if (user.isEmpty()) {
                log.error("Send email error to: {}. User not exists!", notification.getUserId());
                continue;
            }
            String to = user.get().getEmail();
            String commentLink = buildCommentLink(notification.getCommentId());

            String subject;
            String recipientText;

            if (notification.getForAuthor()) {
                subject = "Новый комментарий к вашему посту";
                recipientText = "Пользователь оставил комментарий к вашему посту.";
            } else {
                subject = "Новый комментарий в обсуждении, на которое вы подписаны";
                recipientText = "Появился новый комментарий в обсуждении, на которое вы подписаны.";
            }

            sendEmail(to, subject, "email/comment-notification", Map.of(
                    "commentLink", commentLink,
                    "recipientText", recipientText,
                    "subject", subject
            ));
        }
    }

    @Override
    public void processCreatePostForTagNotifications(List<TagPostNotification> notifications) {
        for (TagPostNotification notification : notifications) {
            Optional<User> user = userService.getUser(notification.getUserId());
            if (user.isEmpty()) {
                log.error("Send email error to: {}. User not exists!", notification.getUserId());
                continue;
            }
            String to = user.get().getEmail();
            String postLink = buildPostLink(notification.getPostId());
            sendEmail(to, "Новый вопрос с меткой, на которую вы подписаны", "email/tag-post-notification", Map.of(
                    "postLink", postLink,
                    "tagName", notification.getTagName()
            ));
        }
    }

    private void sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String body = templateEngine.process(template, context);

            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Send email error to {}. Error: {}", to, e.getMessage(), e);
        }
    }

    private String buildAnswerLink(Long postId, Long answerId) {
        return String.format("%s/questions/%d/#answer-%d", siteUrl, postId, answerId);
    }

    private String buildCommentLink(Long commentId) {
        return String.format("%s/comments/#comment-%d", siteUrl, commentId);
    }

    private String buildPostLink(Long postId) {
        return siteUrl + "/questions/" + postId;
    }
}