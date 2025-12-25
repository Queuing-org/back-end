package queuing.core.room.application.dto;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;

public record GetListRoomCommand(
    Long lastId,
    int size
) {
    public GetListRoomCommand {
        if (lastId != null && lastId <= 0) {
            throw new BusinessException(ErrorCode.COMMON_INVALID_INPUT);
        }

        if (size <= 0) {
            throw new BusinessException(ErrorCode.COMMON_INVALID_INPUT);
        }
    }
}
