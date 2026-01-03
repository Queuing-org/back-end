package queuing.core.global.security.csrf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.util.StringUtils;

import queuing.core.global.security.properties.CsrfProperties;

public final class CustomCsrfTokenRepository implements CsrfTokenRepository {
    private final CookieCsrfTokenRepository cookieCsrfTokenRepository;

    public CustomCsrfTokenRepository(CsrfProperties csrfProperties) {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        if (StringUtils.hasText(csrfProperties.header().name())) {
            cookieCsrfTokenRepository.setHeaderName(csrfProperties.header().name());
        }
        if (StringUtils.hasText(csrfProperties.cookie().name())) {
            cookieCsrfTokenRepository.setCookieName(csrfProperties.cookie().name());
        }
        if (StringUtils.hasText(csrfProperties.cookie().path())) {
            cookieCsrfTokenRepository.setCookiePath(csrfProperties.cookie().path());
        }

        cookieCsrfTokenRepository.setCookieCustomizer(builder -> {
            if (StringUtils.hasText(csrfProperties.cookie().domain())) {
                builder.domain(csrfProperties.cookie().domain());
            }
            builder.sameSite(csrfProperties.cookie().sameSite());
            builder.maxAge(csrfProperties.cookie().maxAge());
            builder.secure(csrfProperties.cookie().secure());
        });

        this.cookieCsrfTokenRepository = cookieCsrfTokenRepository;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return cookieCsrfTokenRepository.generateToken(request);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        cookieCsrfTokenRepository.saveToken(token, request, response);
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return cookieCsrfTokenRepository.loadToken(request);
    }
}
