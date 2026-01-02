package queuing.core.global.security.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.ErrorCode;
import queuing.core.global.response.ResponseBody;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.USER_NOT_AUTHENTICATED;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(errorCode.getStatusCode());

        objectMapper.writeValue(
            response.getWriter(),
            ResponseBody.error(errorCode.getStatusCode(), errorCode.getCode(), errorCode.getMessage())
        );
    }
}
