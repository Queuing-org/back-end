package queuing.core.room.application.model;

import java.time.Instant;
import java.util.List;

public record RoomItem(
    Long id,
    String slug,
    String title,
    boolean isPrivate,
    Instant createdAt,
    List<MusicTagItem> tags
) {
}
