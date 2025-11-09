package queuing.core.room.application.service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import io.undertow.util.BadRequestException;
import queuing.core.room.application.model.CreateRoomCommand;
import queuing.core.room.application.usecase.CreateRoomUseCase;
import queuing.core.room.domain.entity.MusicTag;
import queuing.core.room.domain.entity.Room;
import queuing.core.room.domain.repository.MusicTagRepository;
import queuing.core.room.domain.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomWriteService implements CreateRoomUseCase {
    private final RoomRepository roomRepository;
    private final MusicTagRepository musicTagRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String create(CreateRoomCommand cmd) {
        try {
            cmd.validate();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }

        Set<MusicTag> tags = new HashSet<>(musicTagRepository.findAllBySlugIn(cmd.slugs()));

        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder slug = new StringBuilder(8);

        for (int i=0; i<8; i++) {
            slug.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        Room room = Room.builder()
            .slug(slug.toString())
            .title(cmd.title())
            .password(cmd.password() != null && !cmd.password().isBlank() ? passwordEncoder.encode(cmd.password()) : null)
            .owner(null)
            .build();
        tags.forEach(room::addTag);

        roomRepository.save(room);

        return room.getSlug();
    }
}
