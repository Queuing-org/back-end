package queuing.core.user.presentation.response;

import queuing.core.user.application.dto.UserProfileDto;
import queuing.core.user.domain.entity.User;

public record UserProfileResponse(
    String nickname,
    String slug,
    String profileImageUrl
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
            user.getNickname(),
            user.getSlug(),
            user.getProfileImageUrl()
        );
    }

    public static UserProfileResponse from(UserProfileDto dto) {
        return new UserProfileResponse(
            dto.nickname(),
            dto.slug(),
            dto.profileImageUrl()
        );
    }
}
