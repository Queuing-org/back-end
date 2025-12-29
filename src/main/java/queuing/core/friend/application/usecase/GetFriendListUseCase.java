package queuing.core.friend.application.usecase;

import queuing.core.friend.application.dto.FriendSummary;
import queuing.core.friend.application.dto.GetFriendListCommand;
import queuing.core.global.dto.SliceResult;

public interface GetFriendListUseCase {
    SliceResult<FriendSummary> getFriendList(GetFriendListCommand command);
}