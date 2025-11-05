package queuing.core.room.domain.repository;

import queuing.core.room.domain.projection.CursorResult;
import queuing.core.room.domain.projection.RoomWithTagsView;

public interface RoomRepositoryCustom {
    CursorResult<RoomWithTagsView> findAllWithTags(Long cursorId, int limit);
}
