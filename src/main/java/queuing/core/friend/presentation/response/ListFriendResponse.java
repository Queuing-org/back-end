package queuing.core.friend.presentation.response;

import java.util.List;

public record ListFriendResponse(
    List<FriendResponse> items,
    boolean hasNext
) {
    public static ListFriendResponse of(List<FriendResponse> items, boolean hasNext) {
        return new ListFriendResponse(items, hasNext);
    }
}
