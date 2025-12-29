package queuing.core.friend.domain.repository;

import queuing.core.friend.domain.entity.Friend;
import queuing.core.global.dto.SliceResult;

public interface FriendRepositoryCustom {
    SliceResult<Friend> findFriendsByUserId(Long userId, Long lastId, int size);
}
