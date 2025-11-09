package queuing.core.user.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
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
                    .nickname(cmd.nickname())
                    .profileImageUrl(cmd.profileImageUrl())
                    .build();

                return userRepository.save(created);
            });

        return user;
    }
}
