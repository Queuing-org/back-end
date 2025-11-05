package queuing.core.room.domain.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import queuing.core.user.domain.entity.User;

@Entity
@Table(
    name = "rooms",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_rooms_slug", columnNames = "slug")
    },
    indexes = {
        @Index(name = "idx_rooms_on_owner_id_and_created_at_desc", columnList = "owner_id, created_at")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug", nullable = true, length = 64)
    private String slug;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "password", length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "owner_id", nullable = true, foreignKey = @ForeignKey(name = "fk_rooms_owner_id"))
    private User owner;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomMusicTag> roomMusicTags = new HashSet<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @Builder
    public Room(String slug, String title, String password, User owner) {
        this.slug = slug;
        this.title = title;
        this.password = password;
        this.owner = owner;
    }

    public void addTag(MusicTag musicTag) {
        RoomMusicTag link = new RoomMusicTag(this, musicTag);
        this.roomMusicTags.add(link);
    }
}
