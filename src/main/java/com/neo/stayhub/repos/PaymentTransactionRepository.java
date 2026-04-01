package com.neo.stayhub.repos;

import com.neo.stayhub.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    PaymentTransaction findFirstByPaymentId(Long id);

}
