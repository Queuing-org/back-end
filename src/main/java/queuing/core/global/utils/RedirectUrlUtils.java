package queuing.core.global.utils;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class RedirectUrlUtils {
    private RedirectUrlUtils() {}

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";

    private static final int PORT_UNSPECIFIED = -1;
    private static final int DEFAULT_HTTP_PORT = 80;
    private static final int DEFAULT_HTTPS_PORT = 443;

    /**
     * 허용되지 않은 URL로의 리다이렉트를 방지하기 위해 URL HTTP 스키마인지, 절대 경로인지, 허용된 출처인지 검증합니다.
     *
     * @param rawUrl 검증할 URL
     * @param allowedOrigins 허용된 출처 목록
     * @return 검증이 완료된 URL
     * @throws IllegalArgumentException 허용하지 않는 입력인 경우
     */
    public static String validateAbsoluteRedirectUrl(String rawUrl, List<String> allowedOrigins) {
        String url = requireNonBlank(rawUrl, "URL 값은 필수 항목이에요.");
        rejectCrlf(url);

        URI target = parseUriOrThrow(url, "잘못된 입력 값이에요.");

        if (!target.isAbsolute()) {
            throw new IllegalArgumentException("URL 값은 절대 경로로 입력해야 해요.");
        }

        Origin targetOrigin = Origin.from(target);
        Set<Origin> allowedOriginSet = parseAllowedOriginsOrThrow(allowedOrigins);

        if (!allowedOriginSet.contains(targetOrigin)) {
            throw new IllegalArgumentException("허용되지 않는 출처예요.");
        }

        return target.toString();
    }

    /**
     * URI에서 출처를 추출합니다.
     *
     * @param uri 추출할 URI
     * @return 기본 포트를 생략한 출처 URI
     */
    public static URI extractOrigin(URI uri) {
        Origin origin = Origin.from(uri);
        return URI.create(origin.toOriginStringWithoutDefaultPort());
    }

    private static String requireNonBlank(String s, String message) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return s;
    }

    private static void rejectCrlf(String s) {
        if (s.indexOf('\r') >= 0 || s.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("잘못된 입력 값이에요.");
        }
    }

    private static URI parseUriOrThrow(String raw, String message) {
        try {
            return URI.create(raw);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private static Set<Origin> parseAllowedOriginsOrThrow(List<String> allowedOrigins) {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            throw new IllegalArgumentException("허용하는 출처는 필수 항목이에요.");
        }

        Set<Origin> set = new HashSet<>();
        for (String raw : allowedOrigins) {
            if (raw == null || raw.isBlank()) {
                continue;
            }

            URI uri = parseUriOrThrow(raw.trim(), "잘못된 입력 값이에요.");
            if (!uri.isAbsolute()) {
                throw new IllegalArgumentException("URL 값은 절대 경로로 입력해야 해요.");
            }

            set.add(Origin.from(uri));
        }
        return set;
    }

    private record Origin(
        String scheme,
        String host,
        int port
    ) {
        static Origin from(URI uri) {
            String scheme = normalizeLower(uri.getScheme());
            validateSchemeOrThrow(scheme);

            String host = normalizeLower(uri.getHost());
            if (host.isBlank()) {
                throw new IllegalArgumentException("잘못된 입력 값이에요.");
            }

            int port = normalizePort(uri.getPort(), scheme);
            return new Origin(scheme, host, port);
        }

        String toOriginStringWithoutDefaultPort() {
            boolean omitPort = isDefaultPort(scheme, port);
            return scheme + "://" + host + (omitPort ? "" : ":" + port);
        }

        private static void validateSchemeOrThrow(String scheme) {
            if (!SCHEME_HTTP.equals(scheme) && !SCHEME_HTTPS.equals(scheme)) {
                throw new IllegalArgumentException("지원하지 않는 URL 스킴이에요.");
            }
        }

        private static int normalizePort(int port, String scheme) {
            if (port != PORT_UNSPECIFIED) {
                return port;
            }
            return SCHEME_HTTPS.equals(scheme) ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT;
        }

        private static boolean isDefaultPort(String scheme, int port) {
            return (SCHEME_HTTPS.equals(scheme) && port == DEFAULT_HTTPS_PORT)
                || (SCHEME_HTTP.equals(scheme) && port == DEFAULT_HTTP_PORT);
        }

        private static String normalizeLower(String s) {
            return (s == null) ? "" : s.toLowerCase(Locale.ROOT);
        }
    }
}
