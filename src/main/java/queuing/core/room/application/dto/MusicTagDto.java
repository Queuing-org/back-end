package queuing.core.room.application.dto;

import queuing.core.room.domain.entity.MusicTag;
import queuing.core.room.domain.query.MusicTagQueryResult;

public record MusicTagDto(
    String slug,
    String name
) {
    public static MusicTagDto from(MusicTag entity) {
        return new MusicTagDto(entity.getSlug(), entity.getName());
    }

    public static MusicTagDto from(MusicTagQueryResult queryResult) {
        return new MusicTagDto(queryResult.slug(), queryResult.name());
    }
}
