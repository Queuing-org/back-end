package queuing.core.friend.application.dto;

public record GetFriendListCommand(
    String userSlug,
    Long lastId,
    int size
) {}
