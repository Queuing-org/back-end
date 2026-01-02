package queuing.core.global.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "core.xsrf")
public record CsrfProperties(
    Header header,
    Cookie cookie
) {
    public record Header(
        String name
    ) {
    }

    public record Cookie(
        String headerName,
        String name,
        String domain,
        String path,
        int maxAge,
        boolean secure,
        String sameSite
    ) {
    }
}
