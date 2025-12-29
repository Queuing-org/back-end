package queuing.core.global.security.rememberme;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, String> {
    Optional<RememberMeToken> findBySeries(String series);

    void deleteByUsername(String username);
}
