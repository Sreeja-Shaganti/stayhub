package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findFirstByUserId(Long id);

}
