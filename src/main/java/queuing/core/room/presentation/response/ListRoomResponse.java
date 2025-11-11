package queuing.core.room.presentation.response;

import java.util.List;

public record ListRoomResponse(
    List<RoomSummaryResponse> rooms,
    boolean hasNext
) {
}
