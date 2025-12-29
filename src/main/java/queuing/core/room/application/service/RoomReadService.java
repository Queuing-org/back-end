package queuing.core.room.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.room.application.dto.GetListRoomCommand;
import queuing.core.room.application.dto.RoomSummary;
import queuing.core.room.application.usecase.GetListRoomUseCase;
import queuing.core.global.dto.SliceResult;
import queuing.core.room.domain.query.RoomQueryResult;
import queuing.core.room.domain.repository.RoomRepositoryCustom;

@Service
@RequiredArgsConstructor
public class RoomReadService implements GetListRoomUseCase {
    private final RoomRepositoryCustom roomRepository;

    @Override
    @Transactional(readOnly = true)
    public SliceResult<RoomSummary> getList(GetListRoomCommand cmd) {
        SliceResult<RoomQueryResult> result = roomRepository.findAllWithTags(cmd.lastId(), cmd.size());
        
        List<RoomSummary> summaries = result.items().stream()
            .map(RoomSummary::from)
            .toList();

        return SliceResult.of(summaries, result.hasNext());
    }
}
