package queuing.core.friend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import queuing.core.friend.domain.entity.Friend;
import queuing.core.friend.domain.entity.FriendStatus;
import queuing.core.user.domain.entity.User;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

    boolean existsByRequesterAndReceiver(User requester, User receiver);

    Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);

    // Find a relationship in either direction
    @Query("SELECT f FROM Friend f WHERE (f.requester = :user1 AND f.receiver = :user2) OR (f.requester = :user2 AND f.receiver = :user1)")
    Optional<Friend> findRelationship(@Param("user1") User user1, @Param("user2") User user2);

    // Find pending requests received by user
    Page<Friend> findByReceiverAndStatus(User receiver, FriendStatus status, Pageable pageable);
}
