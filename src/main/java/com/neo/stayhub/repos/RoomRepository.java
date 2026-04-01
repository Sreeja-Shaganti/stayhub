package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findFirstByHotelId(Long id);

    Room findFirstByRoomTypeId(Long id);

    List<Room> findAllByRoomAmenityAmenitiesId(Long id);

}
