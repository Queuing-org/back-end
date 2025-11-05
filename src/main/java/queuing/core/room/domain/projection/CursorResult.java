package queuing.core.room.domain.projection;

import java.util.List;

public record CursorResult<T>(
    List<T> items,
    Long nextId
) {

}
