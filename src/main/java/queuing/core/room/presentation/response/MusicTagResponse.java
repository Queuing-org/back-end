package queuing.core.room.presentation.response;

import queuing.core.room.application.model.MusicTagItem;

public record MusicTagResponse(
    String slug,
    String name
) {
    public static MusicTagResponse from(MusicTagItem item) {
        return new MusicTagResponse(item.slug(), item.name());
    }
}
