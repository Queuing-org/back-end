package queuing.core.global.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import queuing.core.global.security.Constants;

public final class CsrfTokenIssueFilter extends OncePerRequestFilter {
    private static final String CACHE_CONTROL_VALUE_NO_STORE = "no-store";
    private static final RequestMatcher CSRF_ISSUE_REQUEST_GET =
        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, Constants.Paths.CSRF_ISSUE);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !CSRF_ISSUE_REQUEST_GET.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException {

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            token.getToken();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_VALUE_NO_STORE);
    }
}
