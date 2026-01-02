package queuing.core.global.security.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "core.redirect")
public record RedirectProperties(
    String baseUrl,
    List<String> allowedOrigins
) {
}
