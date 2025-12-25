package queuing.core.room.application.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;
import queuing.core.global.utils.SlugUtils;
import queuing.core.room.application.dto.CreateRoomCommand;
import queuing.core.room.application.usecase.CreateRoomUseCase;
import queuing.core.room.domain.entity.MusicTag;
import queuing.core.room.domain.entity.Room;
import queuing.core.room.domain.repository.MusicTagRepository;
import queuing.core.room.domain.repository.RoomRepository;
import queuing.core.user.domain.entity.User;
import queuing.core.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RoomWriteService implements CreateRoomUseCase {
    private final RoomRepository roomRepository;
    private final MusicTagRepository musicTagRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String create(CreateRoomCommand cmd) {
        // 사용자 조회
        User owner = userRepository.findBySlug(cmd.userSlug())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 태그 조회
        Set<MusicTag> tags = new HashSet<>(musicTagRepository.findAllBySlugIn(cmd.slugs()));

        // 방 생성
        String roomSlug = SlugUtils.generateSlug();
        String encodedPassword = encodePasswordIfPresent(cmd.password());
        Room room = Room.builder()
            .slug(roomSlug)
            .title(cmd.title())
            .password(encodedPassword)
            .owner(owner)
            .build();
        tags.forEach(room::addTag);

        roomRepository.save(room);

        return room.getSlug();
    }

    private String encodePasswordIfPresent(String password) {
        if (password != null && !password.isBlank()) {
            return passwordEncoder.encode(password);
        }
        return null;
    }
}
