package queuing.core.global.security;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import queuing.core.user.application.command.SignUpCommand;
import queuing.core.user.application.service.AuthenticationService;
import queuing.core.user.domain.entity.OAuthProvider;
import queuing.core.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final AuthenticationService authenticationService;

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(oidcUserRequest);
        try {
            return process(oidcUserRequest, oidcUser);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser process(OidcUserRequest oidcUserRequest, OidcUser oidcUser) {
        String registrationId = oidcUserRequest.getClientRegistration().getRegistrationId();

        OAuthProvider oauthProvider = null;
        try {
            oauthProvider = OAuthProvider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        User user = authenticationService.signUp(SignUpCommand.from(oauthProvider, oidcUser));

        return UserPrincipal.create(user, oidcUser);
    }

}
