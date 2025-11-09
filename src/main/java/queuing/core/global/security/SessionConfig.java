package queuing.core.global.security;

import javax.sql.DataSource;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SessionConfig {
    private final SessionProperties sessionProperties;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);

        return persistentTokenRepository;
    }

    // https://docs.spring.io/spring-session/reference/configuration/common.html
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(sessionProperties.sessionCookie().name());
        serializer.setDomainName(sessionProperties.sessionCookie().domain());
        serializer.setCookieMaxAge(sessionProperties.sessionCookie().maxAge());
        serializer.setUseSecureCookie(sessionProperties.sessionCookie().secure());
        serializer.setSameSite(sessionProperties.sessionCookie().sameSite());

        return serializer;
    }

    // https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.embedded-container.customizing.samesite
    @Bean
    public CookieSameSiteSupplier remeberMeCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofLax().whenHasNameMatching(sessionProperties.rememberMeCookie().name());
    }
}
