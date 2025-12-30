package queuing.core.global.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "core.redirect")
public record RedirectProperties(
    String baseUri,
    List<String> allowedOrigins
) {
}
