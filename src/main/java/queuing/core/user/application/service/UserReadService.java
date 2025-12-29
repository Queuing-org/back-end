package queuing.core.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.application.usecase.GetUserProfileUseCase;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService implements GetUserProfileUseCase {
    private final UserRepository userRepository;

    @Override
    public User getUserProfile(String slug) {
        return userRepository.findBySlug(slug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
