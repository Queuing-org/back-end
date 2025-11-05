package queuing.core.room.presentation.response;

import java.util.List;

public record ListMusicTagResponse(
    List<MusicTagResponse> tags
) {
}
