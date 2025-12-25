package queuing.core.room.application.dto;

import java.time.Instant;
import java.util.List;

import queuing.core.room.domain.query.RoomQueryResult;

public record RoomSummary(
    Long id,
    String slug,
    String title,
    boolean isPrivate,
    Instant createdAt,
    List<MusicTagDto> tags
) {
    public static RoomSummary from(RoomQueryResult queryResult) {
        return new RoomSummary(
            queryResult.id(),
            queryResult.slug(),
            queryResult.title(),
            queryResult.isPrivate(),
            queryResult.createdAt(),
            queryResult.tags().stream()
                .map(MusicTagDto::from)
                .toList()
        );
    }
}
