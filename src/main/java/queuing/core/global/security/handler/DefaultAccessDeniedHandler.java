package queuing.core.global.security.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.ErrorCode;
import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.exception.UserOnboardingRequiredException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.USER_INSUFFICIENT_SCOPE;
        if (accessDeniedException instanceof UserOnboardingRequiredException) {
            errorCode = ErrorCode.USER_ONBOARDING_REQUIRED;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(errorCode.getStatusCode());

        objectMapper.writeValue(
            response.getWriter(),
            ResponseBody.error(errorCode.getStatusCode(), errorCode.getCode(), errorCode.getMessage())
        );
    }

}
