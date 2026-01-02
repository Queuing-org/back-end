package queuing.core.global.security.authorization;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import queuing.core.user.domain.entity.User;

public class UserPrincipal implements OAuth2User, OidcUser, UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";

    private final String slug;
    private final String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private OidcIdToken idToken;

    public UserPrincipal(String slug, String email, Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, OidcIdToken idToken) {
        this.slug = slug;
        this.email = email;
        this.authorities = authorities;
        this.attributes = attributes;
        this.idToken = idToken;
    }

    public UserPrincipal(String slug, String email, Collection<? extends GrantedAuthority> authorities) {
        this.slug = slug;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user, OidcUser oidcUser) {
        return new UserPrincipal(
            user.getSlug(),
            user.getEmail(),
            List.of(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name())),
            oidcUser.getAttributes(),
            oidcUser.getIdToken()
        );
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
            user.getSlug(),
            user.getEmail(),
            List.of(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name()))
        );
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return Optional.of(this.slug).orElse("anonymousUser");
    }

    @Override
    public String getUsername() {
        return Optional.of(this.slug).orElse("anonymousUser");
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return new OidcUserInfo(this.getClaims());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
