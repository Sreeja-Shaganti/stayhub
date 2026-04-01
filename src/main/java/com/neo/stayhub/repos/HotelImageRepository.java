package com.neo.stayhub.repos;

import com.neo.stayhub.domain.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {

    HotelImage findFirstByHotelId(Long id);

}
