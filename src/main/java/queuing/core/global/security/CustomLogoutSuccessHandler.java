package queuing.core.global.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedirectProperties redirectProperties;

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        // ✅ 필터가 검증한 continue가 있으면 그쪽으로, 없으면 baseUri로
        Object attr = request.getAttribute(ContinueValidationFilter.ATTR_VALIDATED_CONTINUE_URL);
        String validatedContinueUrl = (attr instanceof String s && !s.isBlank()) ? s : null;

        String redirectUrl = (validatedContinueUrl != null) ? validatedContinueUrl : redirectProperties.baseUri();
        response.sendRedirect(redirectUrl);
    }
}
