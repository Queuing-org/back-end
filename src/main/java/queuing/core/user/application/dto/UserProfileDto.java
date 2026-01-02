package queuing.core.user.application.dto;

import queuing.core.user.domain.entity.User;

public record UserProfileDto(
    String nickname,
    String slug,
    String profileImageUrl
) {
    public static UserProfileDto from(User user) {
        return new UserProfileDto(
            user.getNickname(),
            user.getSlug(),
            user.getProfileImageUrl()
        );
    }
}
