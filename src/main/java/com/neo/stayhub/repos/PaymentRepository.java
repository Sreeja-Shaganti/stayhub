package com.neo.stayhub.repos;

import com.neo.stayhub.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findFirstByBookingId(Long id);

}
