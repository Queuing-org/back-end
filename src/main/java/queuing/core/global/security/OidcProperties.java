package queuing.core.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "core.oauth2")
public record OidcProperties(
    String successRedirectUri,
    String failureRedirectUri
) {
}
