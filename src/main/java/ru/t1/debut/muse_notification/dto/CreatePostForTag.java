package ru.t1.debut.muse_notification.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreatePostForTag extends EventMessage {
    private Long postId;
    private String tagName;

    public CreatePostForTag(Set<UUID> receivers, Long postId, String tagName) {
        super(EventType.NEW_POST_FOR_TAG, receivers);
        this.postId = postId;
        this.tagName = tagName;
    }
}
