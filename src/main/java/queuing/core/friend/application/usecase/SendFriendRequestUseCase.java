package queuing.core.friend.application.usecase;

public interface SendFriendRequestUseCase {
    void sendRequest(String requesterSlug, String targetSlug);
}