package queuing.core.room.application.usecase;

import java.util.List;

import queuing.core.room.application.dto.MusicTagDto;

public interface GetListMusicTagUseCase {
    List<MusicTagDto> getList();
}
