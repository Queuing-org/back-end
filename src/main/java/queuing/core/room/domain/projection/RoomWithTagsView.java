package queuing.core.room.domain.projection;

import java.time.Instant;
import java.util.List;

public record RoomWithTagsView(
    Long id,
    String slug,
    String title,
    boolean isPrivate,
    Instant createdAt,
    List<MusicTagView> tags
) {
}
