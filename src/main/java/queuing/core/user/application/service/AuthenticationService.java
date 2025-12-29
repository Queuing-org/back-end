package queuing.core.user.application.service;

import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.application.command.CheckOnboardingCompletedQuery;
import queuing.core.user.application.command.SignUpCommand;
import queuing.core.user.application.usecase.SignUpUseCase;
import queuing.core.user.domain.entity.Role;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements SignUpUseCase {
    private final UserRepository userRepository;

    @Transactional
    public User signUp(SignUpCommand cmd) {
        User user = userRepository.findByProviderAndProviderId(cmd.oauthProvider(), cmd.oauthProviderId())
            .orElseGet(() -> {
                String slug = String.valueOf(UUID.randomUUID());
                int guard = 0;
                while (userRepository.existsBySlug(slug) && guard++ < 5) {
                    slug = String.valueOf(UUID.randomUUID());
                }

                User created = User.builder()
                    .slug(slug)
                    .provider(cmd.oauthProvider())
                    .providerId(cmd.oauthProviderId())
                    .email(cmd.email())
                    .nickname(null)
                    .profileImageUrl(cmd.profileImageUrl())
                    .role(Role.USER)
                    .build();

                return userRepository.save(created);
            });

        return user;
    }

    @Override
    @Cacheable(cacheNames = "profileCompleted", key = "#slug")
    public boolean isOnboardingCompleted(CheckOnboardingCompletedQuery query) {
        User user = userRepository.findBySlug(query.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return user.isOnboardingCompleted();
    }
}
