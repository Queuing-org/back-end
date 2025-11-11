package queuing.core.room.application.model;

import java.util.List;

import jakarta.annotation.Nullable;

public record GetListRoomQuery(
    @Nullable List<String> tags,
    TagMatchType matchType,
    @Nullable Long roomId,
    int size
) {
    public GetListRoomQuery {
        tags = (tags != null)
            ? tags.stream()
            .map(String::strip)
            .filter(tag -> !tag.isBlank())
            .distinct()
            .toList()
            : List.of();
    }

    public void validate() {
        if (roomId != null && roomId <= 0) {
            throw new IllegalArgumentException("식별자는 1 이상의 정수여야 합니다.");
        }

        if (size < 0) {
            throw new IllegalArgumentException("한 번에 가져올 양은 0 이상의 정수여야 합니다.");
        }
    }
}
