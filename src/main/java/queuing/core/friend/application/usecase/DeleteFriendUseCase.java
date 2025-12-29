package queuing.core.friend.application.usecase;

public interface DeleteFriendUseCase {
    void deleteFriend(String userSlug, String targetSlug);
}