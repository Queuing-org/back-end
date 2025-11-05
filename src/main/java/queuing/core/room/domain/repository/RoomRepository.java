package queuing.core.room.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import queuing.core.room.domain.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
