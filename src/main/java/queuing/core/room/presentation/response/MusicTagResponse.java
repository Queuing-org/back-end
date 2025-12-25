package queuing.core.room.presentation.response;

import queuing.core.room.application.dto.MusicTagDto;

public record MusicTagResponse(
    String slug,
    String name
) {
    public static MusicTagResponse from(MusicTagDto dto) {
        return new MusicTagResponse(dto.slug(), dto.name());
    }
}
