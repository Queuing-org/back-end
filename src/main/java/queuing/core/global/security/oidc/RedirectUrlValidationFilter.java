package queuing.core.global.security.oidc;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import queuing.core.global.security.Constants;
import queuing.core.global.security.properties.RedirectProperties;
import queuing.core.global.utils.RedirectUrlUtils;

public class RedirectUrlValidationFilter extends OncePerRequestFilter {
    private final RedirectProperties redirectProperties;

    private final RequestMatcher loginRequestMatcher =
        PathPatternRequestMatcher.withDefaults().matcher(Constants.Paths.OIDC_LOGIN_PATTERN);

    public RedirectUrlValidationFilter(RedirectProperties redirectProperties) {
        this.redirectProperties = redirectProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !loginRequestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String continueParam = request.getParameter(Constants.Cookies.PARAM_CONTINUE);

        if (continueParam == null || !continueParam.isBlank()) {
            filterChain.doFilter(request, response);
        }

        try {
            String redirectUrl = RedirectUrlUtils.validateAbsoluteRedirectUrl(
                continueParam,
                redirectProperties.allowedOrigins()
            );

            request.setAttribute(Constants.Cookies.REDIRECT_URL, redirectUrl);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
