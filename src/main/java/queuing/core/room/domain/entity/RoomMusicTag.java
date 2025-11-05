package queuing.core.room.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms_music_tags",
    indexes = @Index(name = "idx_rooms_music_tags_on_tag_id_and_room_id", columnList = "tag_id, room_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMusicTag {
    @EmbeddedId
    private RoomTagId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "fk_rooms_music_tags_room_id"))
    private Room room;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_rooms_music_tags_tag_id"))
    private MusicTag tag;

    public RoomMusicTag(Room room, MusicTag tag) {
        this.room = room;
        this.tag = tag;
        this.id = new RoomTagId();
    }
}
