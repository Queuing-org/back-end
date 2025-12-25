package queuing.core.user.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.domain.entity.Role;
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
                    .role(Role.GUEST)
                    .build();

                return userRepository.save(created);
            });

        return user;
    }

    @Transactional
    public User updateNickname(UpdateNicknameCommand cmd) {
        User user = userRepository.findBySlug(cmd.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!cmd.nickname().equals(user.getNickname()) && userRepository.existsByNickname(cmd.nickname())) {
            throw new BusinessException(ErrorCode.USER_NICKNAME_DUPLICATED);
        }

        user.updateNickname(cmd.nickname());

        return user;
    }
}
