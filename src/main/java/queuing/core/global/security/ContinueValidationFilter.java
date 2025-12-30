package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import queuing.core.global.utils.RedirectUriUtils;

public class ContinueValidationFilter extends OncePerRequestFilter {
    // ✅ “검증된 continue URL”이라는 의미가 명확한 이름
    public static final String ATTR_VALIDATED_CONTINUE_URL = "VALIDATED_CONTINUE_URL";

    private static final String LOGOUT_URL = "/api/auth/logout";

    private final RedirectProperties redirectProperties;

    private final RequestMatcher loginRequestMatcher =
        PathPatternRequestMatcher.withDefaults().matcher("/api/auth/login/**");

    private final RequestMatcher logoutRequestMatcher =
        PathPatternRequestMatcher.withDefaults().matcher("/api/auth/logout");

    public ContinueValidationFilter(RedirectProperties redirectProperties) {
        this.redirectProperties = redirectProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(loginRequestMatcher.matches(request) || logoutRequestMatcher.matches(request));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        boolean isLoginRequest = loginRequestMatcher.matches(request);
        boolean isLogoutRequest = logoutRequestMatcher.matches(request);

        String continueUrlParam = request.getParameter(ContinueSavingAuthorizationRequestResolver.PARAM_CONTINUE);

        if (continueUrlParam != null && !continueUrlParam.isBlank()) {
            try {
                String validatedContinueUrl = RedirectUriUtils.validateAbsoluteUriOrThrow(
                    continueUrlParam,
                    redirectProperties.allowedOrigins()
                );
                request.setAttribute(ATTR_VALIDATED_CONTINUE_URL, validatedContinueUrl);

            } catch (IllegalArgumentException e) {
                if (isLoginRequest) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
