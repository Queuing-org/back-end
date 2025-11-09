package queuing.core.room.presentation.response;

import java.time.Instant;
import java.util.List;

public record RoomSummaryResponse(
    long id,
    String slug,
    String title,
    boolean isPrivate,
    Instant createdAt,
    List<MusicTagResponse> tags
) {
}
