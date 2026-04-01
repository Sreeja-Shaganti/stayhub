package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
