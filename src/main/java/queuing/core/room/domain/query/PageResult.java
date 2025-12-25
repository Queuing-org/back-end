package queuing.core.room.domain.query;

import java.util.List;

public record PageResult<T>(
    List<T> items,
    Long nextCursor
) {
    public static <T> PageResult<T> of(List<T> items, Long nextCursor) {
        return new PageResult<>(items, nextCursor);
    }

    public boolean hasNext() {
        return nextCursor != null;
    }
}
