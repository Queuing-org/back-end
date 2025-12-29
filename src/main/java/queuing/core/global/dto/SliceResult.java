package queuing.core.global.dto;

import java.util.List;

public record SliceResult<T>(
    List<T> items,
    boolean hasNext
) {
    public static <T> SliceResult<T> of(List<T> items, boolean hasNext) {
        return new SliceResult<>(items, hasNext);
    }
}
