package queuing.core.user.application.usecase;

import queuing.core.user.domain.entity.User;

public interface GetUserProfileUseCase {
    User getUserProfile(String slug);
}
