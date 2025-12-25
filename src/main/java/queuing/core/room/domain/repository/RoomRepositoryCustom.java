package queuing.core.room.domain.repository;

import queuing.core.room.domain.query.PageResult;
import queuing.core.room.domain.query.RoomQueryResult;

public interface RoomRepositoryCustom {
    PageResult<RoomQueryResult> findAllWithTags(Long cursorId, int limit);
}
