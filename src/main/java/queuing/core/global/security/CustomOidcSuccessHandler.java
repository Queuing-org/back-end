package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOidcSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        String continueRedirectUrl = ContinueSavingAuthorizationRequestResolver.popContinueOrNull(request);
        String redirectUrl = (continueRedirectUrl != null) ? continueRedirectUrl : redirectProperties.baseUri();

        response.sendRedirect(redirectUrl);
    }
}
