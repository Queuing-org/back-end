package queuing.core.room.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import queuing.core.room.application.dto.MusicTagDto;
import queuing.core.room.application.usecase.GetListMusicTagUseCase;
import queuing.core.room.domain.entity.MusicTag;
import queuing.core.room.domain.repository.MusicTagRepository;

@Service
@RequiredArgsConstructor
public class MusicTagReadService implements GetListMusicTagUseCase {
    private final MusicTagRepository musicTagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MusicTagDto> getList() {
        List<MusicTag> tags = musicTagRepository.findAllByOrderBySlugAsc();

        return tags.stream()
            .map(MusicTagDto::from)
            .toList();
    }
}
