package queuing.core.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String slug) throws UsernameNotFoundException {
        User user = userRepository.findBySlug(slug)
            .orElseThrow(() -> new UsernameNotFoundException("해당 슬러그(" + slug + ")를 가진 사용자를 찾을 수 없습니다."));

        return UserPrincipal.create(user);
    }
}
