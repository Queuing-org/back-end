package queuing.core.room.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;

@Entity
@Getter
@Table(
    name = "music_tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_music_tags_slug", columnNames = "slug")
    }
)
public class MusicTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
}
