package org.example.documentservice.repository;

import org.example.documentservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomUuid(String roomUuid);
}
