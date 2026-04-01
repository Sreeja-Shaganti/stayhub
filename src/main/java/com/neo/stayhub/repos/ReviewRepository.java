package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findFirstByBookingId(Long id);

    Review findFirstByHotelId(Long id);

    Review findFirstByUserId(Long id);

}
