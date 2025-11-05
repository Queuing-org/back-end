package queuing.core.room.application.model;

import queuing.core.room.domain.entity.MusicTag;

public record MusicTagItem(
    String slug,
    String name
) {
    public static MusicTagItem from(MusicTag tag) {
        return new MusicTagItem(tag.getSlug(), tag.getName());
    }
}
