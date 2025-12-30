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

import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    private final OidcProperties oidcProperties;
    private final SessionProperties sessionProperties;

    private final OidcUserService oidcUserService;
    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    private final ProfileCompletedAuthorizationManager profileCompletedAuthorizationManager;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new DefaultAuthenticationEntryPoint(objectMapper))
                .accessDeniedHandler(new DefaultAccessDeniedHandler(objectMapper))
            )

            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(a -> a.baseUri("/api/auth/login"))
                .redirectionEndpoint(r -> r.baseUri("/api/auth/callback/*"))
                .userInfoEndpoint(u -> u.oidcUserService(oidcUserService))
                .successHandler(new CustomOidcSuccessHandler(oidcProperties))
                .failureHandler(new CustomOidcFailureHandler(oidcProperties))
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

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID",
                    sessionProperties.sessionCookie().name(),
                    sessionProperties.rememberMeCookie().name()
                )
                .logoutSuccessHandler(new CustomLogoutSuccessHandler(objectMapper))
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS)
                .permitAll()

                .requestMatchers("/api/v1/user-profiles/me/onboarding")
                .authenticated()

                .requestMatchers("/api/**").access(profileCompletedAuthorizationManager)

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
