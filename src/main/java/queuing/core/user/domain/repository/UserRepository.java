package queuing.core.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import queuing.core.user.domain.entity.OAuthProvider;
import queuing.core.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    Optional<User> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByNickname(String nickname);
}
