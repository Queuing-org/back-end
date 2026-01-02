package queuing.core.global.security.oidc;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

import queuing.core.global.security.Constants;
import queuing.core.global.security.properties.RedirectProperties;

@RequiredArgsConstructor
public class CustomOidcSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        Object redirectUrlAttribute = request.getAttribute(Constants.Cookies.REDIRECT_URL);
        request.removeAttribute(Constants.Cookies.REDIRECT_URL);

        String location = (redirectUrlAttribute instanceof String redirectUrl && !redirectUrl.isBlank())
            ? redirectUrl
            : redirectProperties.baseUrl();

        response.sendRedirect(location);
    }
}
