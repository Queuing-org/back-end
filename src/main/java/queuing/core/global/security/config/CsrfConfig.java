package queuing.core.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import queuing.core.global.security.csrf.CustomCsrfTokenRepository;
import queuing.core.global.security.properties.CsrfProperties;

@Configuration
public class CsrfConfig {
    @Bean
    public CsrfTokenRepository csrfTokenRepository(CsrfProperties csrfProperties) {
        return new CustomCsrfTokenRepository(csrfProperties);
    }
}
