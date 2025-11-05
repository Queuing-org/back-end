package queuing.core.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final SessionProperties sessionProperties;

    private final OidcUserService oidcUserService;
    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)

            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(u -> u.oidcUserService(oidcUserService))
                .successHandler((req, res, auth) -> res.sendRedirect("/"))
                .failureHandler((req, res, ex) -> res.sendRedirect("/login?error=" + ex.getMessage()))
            )

            .rememberMe(remember -> remember
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository)
                .rememberMeCookieName(sessionProperties.rememberMeCookie().name())
                .key(sessionProperties.rememberMeCookie().key())
                .rememberMeCookieDomain(sessionProperties.rememberMeCookie().domain())
                .useSecureCookie(sessionProperties.rememberMeCookie().secure())
                .tokenValiditySeconds(sessionProperties.rememberMeCookie().maxAge())
                .alwaysRemember(sessionProperties.rememberMeCookie().alwaysRememberMe())
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS)
                .permitAll()

                .anyRequest()
                .permitAll()
            )

            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
