package queuing.core.room.domain.repository;

import java.util.List;

import queuing.core.room.domain.projection.CursorResult;
import queuing.core.room.domain.projection.RoomWithTagsView;

public interface RoomRepositoryCustom {
    CursorResult<RoomWithTagsView> findAll(List<String> tags, String matchType, Long id, int limit);
}
