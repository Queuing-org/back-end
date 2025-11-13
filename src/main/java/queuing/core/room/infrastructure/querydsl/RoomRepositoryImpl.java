package queuing.core.room.infrastructure.querydsl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import queuing.core.room.domain.entity.QMusicTag;
import queuing.core.room.domain.entity.QRoom;
import queuing.core.room.domain.entity.QRoomMusicTag;
import queuing.core.room.domain.entity.Room;
import queuing.core.room.domain.projection.CursorResult;
import queuing.core.room.domain.projection.MusicTagView;
import queuing.core.room.domain.projection.RoomWithTagsView;
import queuing.core.room.domain.repository.RoomRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public CursorResult<RoomWithTagsView> findAll(List<String> tags, String matchType, Long cursorRoomId, int limit) {
        // ID 목록 조회
        tags = (tags != null) ? tags : List.of();
        boolean isAllMatch = matchType != null && "ALL".equalsIgnoreCase(matchType.strip());
        List<Long> roomIdList = fetchRoomIds(tags, isAllMatch, cursorRoomId, limit);

        // 해당하는 ID가 없으면 빈 리스트 즉시 반환
        if (roomIdList.isEmpty()) {
            return new CursorResult<>(List.of(), null);
        }

        boolean hasNext = roomIdList.size() > limit;
        // 현재 조회에서 찾을 엔티티 ID / 반열린구간 [0, limit)
        List<Long> pageIds = hasNext ? roomIdList.subList(0, limit) : roomIdList;
        // 다음 조회에서 찾을 엔티티 ID / {0, ..., limit - 1 | limit}
        Long nextCursorId = hasNext ? pageIds.get(limit) : null;

        // 방 엔티티 조회
        Map<Long, Room> roomsById = fetchRooms(pageIds);
        // 방 엔티티와 연관된 태그 엔티티 조회
        Map<Long, List<MusicTagView>> tagsByRoom = fetchTagsByRoom(pageIds);

        // 프로젝션 구성
        List<RoomWithTagsView> items = pageIds.stream()
            .map(roomsById::get)
            .filter(Objects::nonNull)
            .map(r -> new RoomWithTagsView(
                r.getId(),
                r.getSlug(),
                r.getTitle(),
                r.getPassword() != null && !r.getPassword().isBlank(),
                r.getCreatedAt(),
                tagsByRoom.getOrDefault(r.getId(), List.of())
            ))
            .toList();

        return new CursorResult<>(items, nextCursorId);
    }

    private List<Long> fetchRoomIds(List<String> tags, boolean allMatch, Long roomId, int limit) {
        BooleanExpression cursor = (roomId != null && roomId > 0L) ? QRoom.room.id.lt(roomId) : null;

        // select r.id from rooms r
        // (where r.id < {roomId})
        // order by r.id desc limit {limit + 1}
        JPAQuery<Long> q = query
            .select(QRoom.room.id)
            .from(QRoom.room)
            .where(cursor)
            .orderBy(QRoom.room.id.desc())
            .limit((long) limit + 1L);

        if (!tags.isEmpty()) {
            // select r.id from rooms r
            // join room_music_tag rmt on rmt.room_id = r.id
            // join music_tag mt on mt.id = rmt.tag_id
            // (where r.id < {roomId} and) mt.slug in {tags}
            // group by r.id
            // order by r.id desc limit {limit + 1}
            q.join(QRoom.room.roomMusicTags, QRoomMusicTag.roomMusicTag)
                .join(QRoomMusicTag.roomMusicTag.tag, QMusicTag.musicTag)
                .where(QMusicTag.musicTag.slug.in(tags))
                .groupBy(QRoom.room.id);

            if (allMatch) {
                // select r.id from rooms r
                // join room_music_tag rmt on rmt.room_id = r.id
                // join music_tag mt on mt.id = rmt.tag_id
                // (where r.id < {roomId} and) mt.slug in {tags}
                // group by r.id having count(distinct mt.id) = {tags.size()}
                // order by r.id desc limit {limit + 1}
                // TODO: 이 방식은 덜 직관적이어 보이므로 협의가 필요
                q.having(QMusicTag.musicTag.id.countDistinct().eq((long) tags.size()));
            }
        }

        return q.fetch();
    }

    private Map<Long, Room> fetchRooms(List<Long> roomIds) {
        if (roomIds.isEmpty()) return Map.of();

        // select * from rooms r
        // where r.id in {roomIds}
        return query
            .selectFrom(QRoom.room)
            .where(QRoom.room.id.in(roomIds))
            .fetch()
            .stream()
            .collect(Collectors.toMap(Room::getId, Function.identity()));
    }

    private Map<Long, List<MusicTagView>> fetchTagsByRoom(List<Long> roomIds) {
        if (roomIds.isEmpty()) return Map.of();

        // select rmt.room_id, mt.slug, mt.name from room_music_tag rmt
        // join music_tag mt on rmt.tag_id = mt.id
        // where rmt.room_id in {roomIds}
        // order by mt.name asc
        return query
            .from(QRoomMusicTag.roomMusicTag)
            .join(QRoomMusicTag.roomMusicTag.tag, QMusicTag.musicTag)
            .where(QRoomMusicTag.roomMusicTag.room.id.in(roomIds))
            .orderBy(QMusicTag.musicTag.name.asc())
            .transform(GroupBy
                .groupBy(QRoomMusicTag.roomMusicTag.room.id)
                .as(GroupBy.list(Projections.constructor(
                    MusicTagView.class,
                    QMusicTag.musicTag.slug,
                    QMusicTag.musicTag.name
                )))
            );
    }
}
