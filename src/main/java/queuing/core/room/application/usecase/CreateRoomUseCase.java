package queuing.core.room.application.usecase;

import queuing.core.room.application.model.CreateRoomCommand;

public interface CreateRoomUseCase {
    String create(CreateRoomCommand cmd);
}
