ALTER TABLE answer_notifications
ADD COLUMN description VARCHAR DEFAULT NULL;

ALTER TABLE comment_notifications
ADD COLUMN description VARCHAR DEFAULT NULL;

ALTER TABLE tag_post_notifications
ADD COLUMN description VARCHAR DEFAULT NULL;