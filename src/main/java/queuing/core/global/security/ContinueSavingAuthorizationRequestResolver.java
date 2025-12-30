package queuing.core.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class ContinueSavingAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    public static final String PARAM_CONTINUE = "continue";
    public static final String SESSION_CONTINUE_KEY = "OIDC_CONTINUE_URL";

    private final RedirectProperties redirectProperties;
    private final OAuth2AuthorizationRequestResolver delegate;

    public ContinueSavingAuthorizationRequestResolver(
        RedirectProperties redirectProperties,
        ClientRegistrationRepository clientRegistrationRepository
    ) {
        this.redirectProperties = redirectProperties;
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository,
            "/api/auth/login"
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        saveContinueIfValidated(request);
        return delegate.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        saveContinueIfValidated(request);
        return delegate.resolve(request, clientRegistrationId);
    }

    private void saveContinueIfValidated(HttpServletRequest request) {
        Object v = request.getAttribute(ContinueValidationFilter.ATTR_VALIDATED_CONTINUE_URL);
        if (!(v instanceof String validated) || validated.isBlank()) {
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_CONTINUE_KEY, validated);
    }

    public static String popContinueOrNull(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        Object v = session.getAttribute(SESSION_CONTINUE_KEY);
        session.removeAttribute(SESSION_CONTINUE_KEY);
        return (v instanceof String s && !s.isBlank()) ? s : null;
    }
}
