package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOidcSuccessHandler implements AuthenticationSuccessHandler {
    private final OidcProperties oidcProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        System.out.println(oidcProperties.successRedirectUri());
        response.sendRedirect(oidcProperties.successRedirectUri());
    }
}
