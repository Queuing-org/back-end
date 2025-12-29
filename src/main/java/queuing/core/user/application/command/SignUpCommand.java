package queuing.core.user.application.command;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import queuing.core.user.domain.entity.OAuthProvider;

public record SignUpCommand(
    OAuthProvider oauthProvider,
    String oauthProviderId,
    String email,
    String profileImageUrl
) {
    public static SignUpCommand from(OAuthProvider oauthProvider, OidcUser oidcUser) {
        return new SignUpCommand(oauthProvider, oidcUser.getSubject(), oidcUser.getEmail(), oidcUser.getPicture());
    }
}
