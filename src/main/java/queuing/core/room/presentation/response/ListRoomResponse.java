package queuing.core.room.presentation.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ListRoomResponse(
    boolean hasNext,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long nextId,
    List<RoomSummaryResponse> rooms
) {
}
