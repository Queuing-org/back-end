package queuing.core.room.application.model;

import java.util.List;

public record GetListRoomResult(
    List<RoomItem> items,
    boolean hasNext,
    Long nextId
) {
}
