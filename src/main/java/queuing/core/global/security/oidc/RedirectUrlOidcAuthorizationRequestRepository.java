package queuing.core.global.security.oidc;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import queuing.core.global.security.Constants;

public class RedirectUrlOidcAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository =
        new HttpSessionOAuth2AuthorizationRequestRepository();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return authorizationRequestRepository.loadAuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(
        OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        if (authorizationRequest == null) {
            authorizationRequestRepository.saveAuthorizationRequest(null, request, response);
            return;
        }

        Object redirectUrlAttribute = request.getAttribute(Constants.Cookies.REDIRECT_URL);

        if (!(redirectUrlAttribute instanceof String redirectUrl) || redirectUrl.isBlank()) {
            authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);
            return;
        }

        Map<String, Object> updatedAttributes = new HashMap<>(authorizationRequest.getAttributes());
        updatedAttributes.put(Constants.Cookies.SESSION_ATTR_REDIRECT_URL, redirectUrl);

        OAuth2AuthorizationRequest updatedAuthorizationRequest = authorizationRequest = OAuth2AuthorizationRequest
            .from(authorizationRequest)
            .attributes(requestAttributes -> {
                requestAttributes.clear();
                requestAttributes.putAll(updatedAttributes);
            })
            .build();

        authorizationRequestRepository.saveAuthorizationRequest(updatedAuthorizationRequest, request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        OAuth2AuthorizationRequest removedAuthorizationRequest = authorizationRequestRepository
            .removeAuthorizationRequest(request, response);

        if (removedAuthorizationRequest == null) {
            return removedAuthorizationRequest;
        }

        Object redirectUrlAttribute = removedAuthorizationRequest.getAttributes()
            .get(Constants.Cookies.SESSION_ATTR_REDIRECT_URL);

        if (!(redirectUrlAttribute instanceof String redirectUrl) || redirectUrl.isBlank()) {
            return removedAuthorizationRequest;
        }

        request.setAttribute(Constants.Cookies.REDIRECT_URL, redirectUrl);

        return removedAuthorizationRequest;
    }
}
