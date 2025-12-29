package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOidcFailureHandler implements AuthenticationFailureHandler {
    @Value("${core.oauth2.failure-redirect-uri}")
    private String redirectUrl = "http://localhost:8080/?error=true";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        response.sendRedirect(redirectUrl);
    }
}
