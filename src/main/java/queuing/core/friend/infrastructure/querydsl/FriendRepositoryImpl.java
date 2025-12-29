package queuing.core.friend.infrastructure.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import queuing.core.friend.domain.entity.Friend;
import queuing.core.friend.domain.entity.FriendStatus;
import queuing.core.friend.domain.entity.QFriend;
import queuing.core.friend.domain.repository.FriendRepositoryCustom;
import queuing.core.global.dto.SliceResult;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public SliceResult<Friend> findFriendsByUserId(Long userId, Long lastId, int size) {
        QFriend friend = QFriend.friend;

        BooleanExpression isFriend = (friend.requester.id.eq(userId).or(friend.receiver.id.eq(userId)))
            .and(friend.status.eq(FriendStatus.ACCEPTED));
        
        BooleanExpression lessThanId = (lastId != null) ? friend.id.lt(lastId) : null;

        List<Friend> friends = query.selectFrom(friend)
            .join(friend.requester).fetchJoin()
            .join(friend.receiver).fetchJoin()
            .where(isFriend, lessThanId)
            .orderBy(friend.id.desc())
            .limit(size + 1L)
            .fetch();

        boolean hasNext = false;
        if (friends.size() > size) {
            friends.remove(size);
            hasNext = true;
        }

        return SliceResult.of(friends, hasNext);
    }
}