package com.neo.stayhub.repos;

import com.neo.stayhub.domain.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRoomRepository extends JpaRepository<BookingRoom, Long> {

    BookingRoom findFirstByBookingId(Long id);

    BookingRoom findFirstByRoomId(Long id);

}
