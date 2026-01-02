package queuing.core.global.security.authorization;

import java.util.function.Supplier;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.security.exception.InvalidPrincipalException;
import queuing.core.global.security.exception.UserOnboardingRequiredException;
import queuing.core.user.application.command.CheckOnboardingCompletedQuery;
import queuing.core.user.application.usecase.SignUpUseCase;

@Component
@RequiredArgsConstructor
public class OnboardingRequiredAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private final SignUpUseCase signUpUseCase;

    @Override
    public AuthorizationResult authorize(
        Supplier<? extends Authentication> authentication,
        RequestAuthorizationContext context
    ) {
        Authentication auth = authentication.get();
        if (auth == null || trustResolver.isAnonymous(auth)) {
            return new AuthorizationDecision(true);
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new InvalidPrincipalException();
        }

        try {
            boolean completed = signUpUseCase.isOnboardingCompleted(
                new CheckOnboardingCompletedQuery(userPrincipal.getUsername())
            );

            if (!completed) {
                throw new UserOnboardingRequiredException();
            }

            return new AuthorizationDecision(true);

        } catch (BusinessException e) {
            throw new InsufficientAuthenticationException(e.getMessage(), e);
        }
    }
}
