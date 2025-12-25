package queuing.core.room.infrastructure.querydsl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import queuing.core.room.domain.entity.QMusicTag;
import queuing.core.room.domain.entity.QRoom;
import queuing.core.room.domain.entity.QRoomMusicTag;
import queuing.core.room.domain.entity.Room;
import queuing.core.room.domain.query.MusicTagQueryResult;
import queuing.core.room.domain.query.PageResult;
import queuing.core.room.domain.query.RoomQueryResult;
import queuing.core.room.domain.repository.RoomRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public PageResult<RoomQueryResult> findAllWithTags(Long lastId, int size) {
        BooleanExpression where = (lastId != null && lastId != 0L) ? QRoom.room.id.lt(lastId) : null;

        List<Room> rooms = query.selectFrom(QRoom.room)
            .where(where)
            .orderBy(QRoom.room.id.desc())
            .limit(size + 1L)
            .fetch();

        if (rooms.isEmpty()) {
            return PageResult.of(List.of(), null);
        }

        boolean hasNext = rooms.size() > size;
        List<Room> bounded = hasNext ? rooms.subList(0, size) : rooms;
        Long nextCursor = hasNext ? rooms.get(size).getId() : null;

        List<Long> roomIds = bounded.stream().map(Room::getId).toList();

        List<Tuple> tuples = query
            .select(
                QRoomMusicTag.roomMusicTag.room.id,
                QMusicTag.musicTag.slug,
                QMusicTag.musicTag.name
            )
            .from(QRoomMusicTag.roomMusicTag)
            .join(QRoomMusicTag.roomMusicTag.tag, QMusicTag.musicTag)
            .where(QRoomMusicTag.roomMusicTag.room.id.in(roomIds))
            .orderBy(QMusicTag.musicTag.name.asc())
            .fetch();

        Map<Long, List<MusicTagQueryResult>> tagMap = tuples.stream()
            .collect(Collectors.groupingBy(
                t -> t.get(0, Long.class),
                Collectors.mapping(
                    t -> new MusicTagQueryResult(
                        t.get(1, String.class),
                        t.get(2, String.class)
                    ),
                    Collectors.toList()
                )
            ));

        List<RoomQueryResult> items = bounded.stream()
            .map(room -> new RoomQueryResult(
                room.getId(),
                room.getSlug(),
                room.getTitle(),
                room.getPassword() != null && !room.getPassword().isBlank(),
                room.getCreatedAt(),
                tagMap.getOrDefault(room.getId(), List.of())
            ))
            .toList();

        return PageResult.of(items, nextCursor);
    }
}
