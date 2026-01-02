package queuing.core.global.security.exception;

import java.io.Serial;

import org.springframework.security.access.AccessDeniedException;

public class UserOnboardingRequiredException extends AccessDeniedException {
    @Serial
    private static final long serialVersionUID = -3885716231332275688L;

    public UserOnboardingRequiredException() {
        super("Onboarding is required.");
    }
}
