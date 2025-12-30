package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOidcFailureHandler implements AuthenticationFailureHandler {
    private final OidcProperties oidcProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        System.out.println(oidcProperties.failureRedirectUri());
        response.sendRedirect(oidcProperties.failureRedirectUri());
    }
}
