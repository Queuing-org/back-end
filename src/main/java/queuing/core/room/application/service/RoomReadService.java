package queuing.core.room.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.room.application.dto.GetListRoomCommand;
import queuing.core.room.application.dto.RoomSummary;
import queuing.core.room.application.dto.SliceResult;
import queuing.core.room.application.usecase.GetListRoomUseCase;
import queuing.core.room.domain.query.PageResult;
import queuing.core.room.domain.query.RoomQueryResult;
import queuing.core.room.domain.repository.RoomRepositoryCustom;

@Service
@RequiredArgsConstructor
public class RoomReadService implements GetListRoomUseCase {
    private final RoomRepositoryCustom roomRepository;

    @Override
    @Transactional(readOnly = true)
    public SliceResult<RoomSummary> getList(GetListRoomCommand cmd) {
        PageResult<RoomQueryResult> pageResult = roomRepository.findAllWithTags(cmd.lastId(), cmd.size());
        return SliceResult.from(pageResult, RoomSummary::from);
    }
}
