package queuing.core.room.domain.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import queuing.core.room.domain.entity.MusicTag;

public interface MusicTagRepository extends JpaRepository<MusicTag, Long> {
    List<MusicTag> findAllByOrderBySlugAsc();

    Optional<MusicTag> findBySlug(String slug);

    List<MusicTag> findAllBySlugIn(Collection<String> slugs);
}
