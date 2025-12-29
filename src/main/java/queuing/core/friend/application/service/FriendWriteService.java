package queuing.core.friend.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.friend.application.usecase.AcceptFriendRequestUseCase;
import queuing.core.friend.application.usecase.DeleteFriendUseCase;
import queuing.core.friend.application.usecase.SendFriendRequestUseCase;
import queuing.core.friend.domain.entity.Friend;
import queuing.core.friend.domain.entity.FriendStatus;
import queuing.core.friend.domain.repository.FriendRepository;
import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FriendWriteService implements SendFriendRequestUseCase, AcceptFriendRequestUseCase, DeleteFriendUseCase {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public void sendRequest(String requesterSlug, String targetSlug) {
        User requester = userRepository.findBySlug(requesterSlug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User target = userRepository.findBySlug(targetSlug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (requester.equals(target)) {
            throw new BusinessException(ErrorCode.FRIEND_INVALID_REQUEST);
        }

        if (friendRepository.findRelationship(requester, target).isPresent()) {
            throw new BusinessException(ErrorCode.FRIEND_ALREADY_EXISTS);
        }

        Friend friend = Friend.builder()
            .requester(requester)
            .receiver(target)
            .status(FriendStatus.PENDING)
            .build();

        friendRepository.save(friend);
    }

    @Override
    @Transactional
    public void acceptRequest(String userSlug, Long requestId) {
        User user = userRepository.findBySlug(userSlug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Friend friend = friendRepository.findById(requestId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friend.getReceiver().equals(user)) {
            throw new BusinessException(ErrorCode.FRIEND_INVALID_REQUEST);
        }

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new BusinessException(ErrorCode.FRIEND_INVALID_REQUEST);
        }

        friend.updateStatus(FriendStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public void deleteFriend(String userSlug, String targetSlug) {
        User user = userRepository.findBySlug(userSlug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User target = userRepository.findBySlug(targetSlug)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Friend friend = friendRepository.findRelationship(user, target)
            .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friendRepository.delete(friend);
    }
}
