package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findFirstByUserId(Long id);

    Booking findFirstByHotelId(Long id);

}
