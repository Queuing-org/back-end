package queuing.core.friend.application.usecase;

public interface AcceptFriendRequestUseCase {
    void acceptRequest(String userSlug, Long requestId);
}
