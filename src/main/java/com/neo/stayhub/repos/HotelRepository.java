package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
