package queuing.core.friend.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import queuing.core.friend.application.dto.FriendSummary;
import queuing.core.friend.application.dto.GetFriendListCommand;
import queuing.core.friend.application.usecase.GetFriendListUseCase;
import queuing.core.friend.domain.entity.Friend;
import queuing.core.friend.domain.repository.FriendRepository;
import queuing.core.global.dto.SliceResult;
import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadService implements GetFriendListUseCase {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    public SliceResult<FriendSummary> getFriendList(GetFriendListCommand command) {
        User user = userRepository.findBySlug(command.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        SliceResult<Friend> friends = friendRepository.findFriendsByUserId(user.getId(), command.lastId(), command.size());

        List<FriendSummary> summaries = friends.items().stream()
            .map(friend -> FriendSummary.from(friend.getCounterpart(user)))
            .toList();

        return SliceResult.of(summaries, friends.hasNext());
    }
}