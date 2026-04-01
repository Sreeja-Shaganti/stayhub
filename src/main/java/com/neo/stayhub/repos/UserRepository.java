package com.neo.stayhub.repos;

import com.neo.stayhub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
