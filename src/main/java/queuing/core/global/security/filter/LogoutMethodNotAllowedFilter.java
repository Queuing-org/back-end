package queuing.core.global.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.ErrorCode;
import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.Constants;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class LogoutMethodNotAllowedFilter extends OncePerRequestFilter {
    private static final RequestMatcher LOGOUT_REQUEST_ANY_METHOD =
        PathPatternRequestMatcher.withDefaults().matcher(Constants.Paths.LOGOUT);
    private static final RequestMatcher LOGOUT_REQUEST_POST_METHOD =
        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, Constants.Paths.LOGOUT);
    private static final RequestMatcher LOGOUT_REQUEST =
        new AndRequestMatcher(LOGOUT_REQUEST_ANY_METHOD, new NegatedRequestMatcher(LOGOUT_REQUEST_POST_METHOD));

    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        return !LOGOUT_REQUEST.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.COMMON_METHOD_NOT_ALLOWED;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(errorCode.getStatusCode());

        objectMapper.writeValue(
            response.getWriter(),
            ResponseBody.error(errorCode.getStatusCode(), errorCode.getCode(), errorCode.getMessage())
        );
    }
}
