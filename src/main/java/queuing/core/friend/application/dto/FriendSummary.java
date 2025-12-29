package queuing.core.friend.application.dto;

import queuing.core.user.domain.entity.User;

public record FriendSummary(
    Long id,
    String nickname,
    String slug,
    String profileImageUrl
) {
    public static FriendSummary from(User user) {
        return new FriendSummary(
            user.getId(),
            user.getNickname(),
            user.getSlug(),
            user.getProfileImageUrl()
        );
    }
}
