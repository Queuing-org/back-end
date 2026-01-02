package queuing.core.global.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "core.session")
public record SessionProperties(
    SessionCookie sessionCookie,
    RememberMeCookie rememberMeCookie
) {
    public record SessionCookie(
        String name,
        String domain,
        String path,
        int maxAge,
        boolean httpOnly,
        boolean secure,
        String sameSite
    ) {
    }

    public record RememberMeCookie(
        String name,
        String key,
        String domain,
        String path,
        int maxAge,
        boolean httpOnly,
        boolean secure,
        String sameSite,
        boolean alwaysRememberMe
    ) {
    }
}
