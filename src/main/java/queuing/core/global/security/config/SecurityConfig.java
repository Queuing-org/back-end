package queuing.core.global.security.config;

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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import queuing.core.global.security.Constants;
import queuing.core.global.security.authorization.OnboardingRequiredAuthorizationManager;
import queuing.core.global.security.filter.CsrfTokenIssueFilter;
import queuing.core.global.security.filter.LogoutMethodNotAllowedFilter;
import queuing.core.global.security.handler.CustomLogoutSuccessHandler;
import queuing.core.global.security.handler.DefaultAccessDeniedHandler;
import queuing.core.global.security.handler.DefaultAuthenticationEntryPoint;
import queuing.core.global.security.oidc.CustomOidcFailureHandler;
import queuing.core.global.security.oidc.CustomOidcSuccessHandler;
import queuing.core.global.security.oidc.RedirectUrlOidcAuthorizationRequestRepository;
import queuing.core.global.security.oidc.RedirectUrlPreservingRequestResolver;
import queuing.core.global.security.oidc.RedirectUrlValidationFilter;
import queuing.core.global.security.properties.RedirectProperties;
import queuing.core.global.security.properties.SessionProperties;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final CorsConfigurationSource corsConfigurationSource;

    private final RedirectProperties redirectProperties;
    private final SessionProperties sessionProperties;

    private final OidcUserService oidcUserService;
    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;

    private final OnboardingRequiredAuthorizationManager onboardingRequiredAuthorizationManager;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)

            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers(Constants.Paths.OIDC_LOGIN_PATTERN, Constants.Paths.OIDC_CALLBACK_BASE)
            )
            .addFilterAfter(new CsrfTokenIssueFilter(), CsrfFilter.class)

            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new DefaultAuthenticationEntryPoint(objectMapper))
                .accessDeniedHandler(new DefaultAccessDeniedHandler(objectMapper))
            )

            .addFilterBefore(
                new RedirectUrlValidationFilter(redirectProperties),
                OAuth2AuthorizationRequestRedirectFilter.class
            )

            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(a -> a
                    .baseUri(Constants.Paths.OIDC_LOGIN_BASE)
                    .authorizationRequestRepository(new RedirectUrlOidcAuthorizationRequestRepository())
                    .authorizationRequestResolver(
                        new RedirectUrlPreservingRequestResolver(clientRegistrationRepository)
                    )
                )
                .redirectionEndpoint(r -> r
                    .baseUri(Constants.Paths.OIDC_CALLBACK_BASE)
                )
                .userInfoEndpoint(u -> u.oidcUserService(oidcUserService))
                .successHandler(new CustomOidcSuccessHandler(redirectProperties))
                .failureHandler(new CustomOidcFailureHandler(redirectProperties))
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

            .addFilterBefore(new LogoutMethodNotAllowedFilter(objectMapper), LogoutFilter.class)

            .logout(logout -> logout
                .logoutUrl(Constants.Paths.LOGOUT)
                .logoutRequestMatcher(
                    PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, Constants.Paths.LOGOUT)
                )
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID",
                    sessionProperties.sessionCookie().name(),
                    "remember-me",
                    sessionProperties.rememberMeCookie().name()
                )
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS)
                .permitAll()

                .requestMatchers(Constants.Paths.PERMIT_ALL)
                .permitAll()

                .requestMatchers(Constants.Paths.API_USER_PROFILE_ONBOARDING)
                .authenticated()

                .requestMatchers(Constants.Paths.API_ALL)
                .access(onboardingRequiredAuthorizationManager)

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
