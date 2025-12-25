package queuing.core.room.domain.query;

import java.time.Instant;
import java.util.List;

public record RoomQueryResult(
    Long id,
    String slug,
    String title,
    boolean isPrivate,
    Instant createdAt,
    List<MusicTagQueryResult> tags
) {
}
