package queuing.core.global.security.oidc;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

import queuing.core.global.security.Constants;
import queuing.core.global.security.properties.RedirectProperties;

@RequiredArgsConstructor
public class CustomOidcFailureHandler implements AuthenticationFailureHandler {
    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        request.removeAttribute(Constants.Cookies.REDIRECT_URL);

        String location = redirectProperties.baseUrl() + Constants.Frontend.LOGIN_ERROR_PATH;

        response.sendRedirect(location);
    }
}
