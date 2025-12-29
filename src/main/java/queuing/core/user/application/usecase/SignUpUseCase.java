package queuing.core.user.application.usecase;

import queuing.core.user.application.command.CheckOnboardingCompletedQuery;
import queuing.core.user.application.command.SignUpCommand;
import queuing.core.user.domain.entity.User;

public interface SignUpUseCase {
    User signUp(SignUpCommand cmd);

    boolean isOnboardingCompleted(CheckOnboardingCompletedQuery query);
}
