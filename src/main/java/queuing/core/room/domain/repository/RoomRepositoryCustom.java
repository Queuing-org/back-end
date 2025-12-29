package queuing.core.room.domain.repository;

import queuing.core.global.dto.SliceResult;
import queuing.core.room.domain.query.RoomQueryResult;

public interface RoomRepositoryCustom {
    SliceResult<RoomQueryResult> findAllWithTags(Long lastId, int size);
}
