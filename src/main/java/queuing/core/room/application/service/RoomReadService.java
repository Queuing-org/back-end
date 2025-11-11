package queuing.core.room.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.room.application.model.GetListRoomQuery;
import queuing.core.room.application.model.GetListRoomResult;
import queuing.core.room.application.model.MusicTagItem;
import queuing.core.room.application.model.RoomItem;
import queuing.core.room.application.usecase.GetListRoomUseCase;
import queuing.core.room.domain.projection.CursorResult;
import queuing.core.room.domain.projection.RoomWithTagsView;
import queuing.core.room.domain.repository.RoomRepositoryCustom;

@Service
@RequiredArgsConstructor
public class RoomReadService implements GetListRoomUseCase {
    private final RoomRepositoryCustom roomRepository;

    @Override
    @Transactional(readOnly = true)
    public GetListRoomResult getList(GetListRoomQuery cmd) {
        cmd.validate();

        CursorResult<RoomWithTagsView> result = roomRepository.findAll(
            cmd.tags(),
            cmd.matchType().name(),
            cmd.roomId(),
            cmd.size()
        );

        List<RoomItem> items = result.items().stream()
            .map(view -> new RoomItem(
                view.id(),
                view.slug(),
                view.title(),
                view.isPrivate(),
                view.createdAt(),
                view.tags().stream()
                    .map(tag -> new MusicTagItem(tag.slug(), tag.name()))
                    .toList()
            ))
            .toList();

        return new GetListRoomResult(items, result.nextId() != null, (result.nextId() != null) ? result.nextId() : null);
    }
}
