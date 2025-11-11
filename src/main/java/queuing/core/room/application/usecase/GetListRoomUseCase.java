package queuing.core.room.application.usecase;

import queuing.core.room.application.model.GetListRoomQuery;
import queuing.core.room.application.model.GetListRoomResult;

public interface GetListRoomUseCase {
    GetListRoomResult getList(GetListRoomQuery cmd);
}
