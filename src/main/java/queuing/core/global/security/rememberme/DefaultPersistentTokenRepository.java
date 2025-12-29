package queuing.core.global.security.rememberme;

import java.util.Date;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultPersistentTokenRepository implements PersistentTokenRepository {

    private final RememberMeTokenRepository rememberMeTokenRepository;

    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        rememberMeTokenRepository.save(RememberMeToken.builder()
            .series(token.getSeries())
            .username(token.getUsername())
            .token(token.getTokenValue())
            .lastUsed(token.getDate().toInstant())
            .build());
    }

    @Override
    @Transactional
    public void updateToken(
        @NonNull String series,
        @NonNull String tokenValue,
        @NonNull Date lastUsed
    ) {
        rememberMeTokenRepository.findById(series)
            .ifPresent(token ->
                token.update(tokenValue, lastUsed.toInstant())
            );
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public PersistentRememberMeToken getTokenForSeries(
        @NonNull String seriesId
    ) {
        return rememberMeTokenRepository.findById(seriesId)
            .map(e ->
                new PersistentRememberMeToken(
                    e.getUsername(),
                    e.getSeries(),
                    e.getToken(),
                    Date.from(e.getLastUsed())
                )
            )
            .orElse(null);
    }

    @Override
    @Transactional
    public void removeUserTokens(
        @NonNull String username
    ) {
        rememberMeTokenRepository.deleteByUsername(username);
    }
}
