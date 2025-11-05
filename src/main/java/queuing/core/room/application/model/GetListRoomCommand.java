package queuing.core.room.application.model;

public record GetListRoomCommand(
    Long lastId,
    int size
) {
    public void validate() {
        if (lastId != null && lastId <= 0) {
            throw new IllegalArgumentException("식별자는 1 이상의 정수여야 합니다.");
        }

        if (size < 0) {
            throw new IllegalArgumentException("한 번에 가져올 양은 0 이상의 정수여야 합니다.");
        }
    }
}
