package queuing.core.friend.presentation.response;

import queuing.core.friend.application.dto.FriendSummary;

public record FriendResponse(
    Long id,
    String nickname,
    String slug,
    String profileImageUrl
) {
    public static FriendResponse from(FriendSummary summary) {
        return new FriendResponse(
            summary.id(),
            summary.nickname(),
            summary.slug(),
            summary.profileImageUrl()
        );
    }
}
