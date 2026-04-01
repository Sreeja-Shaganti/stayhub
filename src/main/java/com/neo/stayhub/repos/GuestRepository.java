package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GuestRepository extends JpaRepository<Guest, Long> {

    Guest findFirstByBookingId(Long id);

}
