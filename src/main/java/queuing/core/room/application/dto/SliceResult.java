package queuing.core.room.application.dto;

import java.util.List;
import java.util.function.Function;

import queuing.core.room.domain.query.PageResult;

public record SliceResult<T>(
    List<T> items,
    boolean hasNext,
    Long nextCursor
) {
    public static <T> SliceResult<T> of(List<T> items, boolean hasNext, Long nextCursor) {
        return new SliceResult<>(items, hasNext, nextCursor);
    }

    public static <S, T> SliceResult<T> from(PageResult<S> pageResult, Function<S, T> mapper) {
        return new SliceResult<>(
            pageResult.items().stream().map(mapper).toList(),
            pageResult.hasNext(),
            pageResult.nextCursor()
        );
    }
}
