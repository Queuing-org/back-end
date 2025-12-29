package queuing.core.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.application.command.UpdateNicknameCommand;
import queuing.core.user.application.usecase.UpdateUserProfileUseCase;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWriteService implements UpdateUserProfileUseCase {
    private final UserRepository userRepository;

    @Override
    public void updateUserProfile(UpdateNicknameCommand cmd) {
        User user = userRepository.findBySlug(cmd.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!cmd.nickname().equals(user.getNickname()) && userRepository.existsByNickname(cmd.nickname())) {
            throw new BusinessException(ErrorCode.USER_NICKNAME_DUPLICATED);
        }

        user.updateNickname(cmd.nickname());
    }

    @Override
    public void completeOnboarding(UpdateNicknameCommand cmd) {
        User user = userRepository.findBySlug(cmd.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.isOnboardingCompleted()) {
            throw new BusinessException(ErrorCode.USER_INSUFFICIENT_SCOPE);
        }

        if (!cmd.nickname().equals(user.getNickname()) && userRepository.existsByNickname(cmd.nickname())) {
            throw new BusinessException(ErrorCode.USER_NICKNAME_DUPLICATED);
        }

        user.updateNickname(cmd.nickname());
    }
}
