package queuing.core.room.application.usecase;

import java.util.List;

import queuing.core.room.application.model.MusicTagItem;

public interface GetListMusicTagUseCase {
    List<MusicTagItem> getList();
}
