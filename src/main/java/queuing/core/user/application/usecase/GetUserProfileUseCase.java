package queuing.core.user.application.usecase;

import queuing.core.user.application.dto.UserProfileDto;

public interface GetUserProfileUseCase {
    UserProfileDto getUserProfile(String slug);

    boolean isNicknameAvailable(String nickname);
}
