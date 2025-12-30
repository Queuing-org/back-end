package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOidcFailureHandler implements AuthenticationFailureHandler {
    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {

        // ✅ 혹시 저장된 continue가 있으면 정리
        ContinueSavingAuthorizationRequestResolver.popContinueOrNull(request);

        String failureRedirectUrl = redirectProperties.baseUri() + "/login?error";
        response.sendRedirect(failureRedirectUrl);
    }
}
