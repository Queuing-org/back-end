package queuing.core.user.application.usecase;

import queuing.core.user.application.command.UpdateNicknameCommand;

public interface UpdateUserProfileUseCase {
    void completeOnboarding(UpdateNicknameCommand cmd);

    void updateUserProfile(UpdateNicknameCommand cmd);
}
