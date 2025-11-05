package queuing.core.room.application.usecase;

import queuing.core.room.application.model.GetListRoomCommand;
import queuing.core.room.application.model.GetListRoomResult;

public interface GetListRoomUseCase {
    GetListRoomResult getList(GetListRoomCommand cmd);
}
