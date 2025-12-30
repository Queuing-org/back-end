package queuing.core.global.utils;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class RedirectUriUtils {
    public static String validateAbsoluteUriOrThrow(String uri, List<String> allowedOrigins) {
        if (uri == null || uri.isBlank()) {
            throw new IllegalArgumentException("URI 값은 필수 항목이에요.");
        }

        if (uri.contains("\r") || uri.contains("\n")) {
            throw new IllegalArgumentException("잘못된 입력 값이에요.");
        }

        URI target;
        try {
            target = URI.create(uri);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력 값이에요.");
        }

        if (!target.isAbsolute()) {
            throw new IllegalArgumentException("URI 값은 절대 경로로 입력해야 해요.");
        }

        String scheme = lower(target.getScheme());
        if (!"http".equals(scheme) && !"https".equals(scheme)) {
            throw new IllegalArgumentException("지원하지 않는 URI 스킴이에요.");
        }

        URI targetOrigin = originOf(target);
        Set<URI> allowed = parseAllowedOrigins(allowedOrigins);

        if (!allowed.contains(targetOrigin)) {
            throw new IllegalArgumentException("허용되지 않는 출처예요.");
        }

        return target.toString();
    }

    public static URI originOf(URI uri) {
        String scheme = lower(uri.getScheme());
        String host = lower(uri.getHost());
        if (host.isBlank()) {
            throw new IllegalArgumentException("잘못된 입력 값이에요.");
        }

        int port = normalizedPort(uri);
        String origin = scheme + "://" + host + (isDefaultPort(scheme, port) ? "" : ":" + port);
        return URI.create(origin);
    }

    private static Set<URI> parseAllowedOrigins(List<String> allowedOrigins) {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            throw new IllegalArgumentException("허용하는 출처는 필수 항목이에요.");
        }

        Set<URI> set = new HashSet<>();
        for (String s : allowedOrigins) {
            if (s == null || s.isBlank()) {
                continue;
            }

            URI u = URI.create(s.trim());
            if (!u.isAbsolute()) {
                throw new IllegalArgumentException("URI 값은 절대 경로로 입력해야 해요.");
            }

            String scheme = lower(u.getScheme());
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                throw new IllegalArgumentException("지원하지 않는 URI 스킴이에요.");
            }

            set.add(originOf(u));
        }

        return set;
    }

    private static int normalizedPort(URI uri) {
        int port = uri.getPort();
        if (port != -1) {
            return port;
        }

        String scheme = lower(uri.getScheme());
        return "https".equals(scheme) ? 443 : ("http".equals(scheme) ? 80 : -1);
    }

    private static boolean isDefaultPort(String scheme, int port) {
        return ("https".equals(scheme) && port == 443) || ("http".equals(scheme) && port == 80);
    }

    private static String lower(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
