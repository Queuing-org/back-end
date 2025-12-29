package queuing.core.room.application.usecase;

import queuing.core.room.application.dto.GetListRoomCommand;
import queuing.core.room.application.dto.RoomSummary;
import queuing.core.global.dto.SliceResult;

public interface GetListRoomUseCase {
    SliceResult<RoomSummary> getList(GetListRoomCommand cmd);
}
