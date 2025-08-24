CREATE TABLE users(
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL
);

ALTER TABLE answer_notifications
ADD CONSTRAINT fk_answer_notifications_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE comment_notifications
ADD CONSTRAINT fk_comment_notifications_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE tag_post_notifications
ADD CONSTRAINT fk_tag_post_notifications_user
FOREIGN KEY (user_id) REFERENCES users(id);