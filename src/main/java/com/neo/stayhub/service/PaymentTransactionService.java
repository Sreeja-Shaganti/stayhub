package com.neo.stayhub.service;

import com.neo.stayhub.domain.Payment;
import com.neo.stayhub.domain.PaymentTransaction;
import com.neo.stayhub.events.BeforeDeletePayment;
import com.neo.stayhub.model.PaymentTransactionDTO;
import com.neo.stayhub.repos.PaymentRepository;
import com.neo.stayhub.repos.PaymentTransactionRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentRepository paymentRepository;

    public PaymentTransactionService(
            final PaymentTransactionRepository paymentTransactionRepository,
            final PaymentRepository paymentRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentTransactionDTO> findAll() {
        final List<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findAll(Sort.by("id"));
        return paymentTransactions.stream()
                .map(paymentTransaction -> mapToDTO(paymentTransaction, new PaymentTransactionDTO()))
                .toList();
    }

    public PaymentTransactionDTO get(final Long id) {
        return paymentTransactionRepository.findById(id)
                .map(paymentTransaction -> mapToDTO(paymentTransaction, new PaymentTransactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaymentTransactionDTO paymentTransactionDTO) {
        final PaymentTransaction paymentTransaction = new PaymentTransaction();
        mapToEntity(paymentTransactionDTO, paymentTransaction);
        return paymentTransactionRepository.save(paymentTransaction).getId();
    }

    public void update(final Long id, final PaymentTransactionDTO paymentTransactionDTO) {
        final PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentTransactionDTO, paymentTransaction);
        paymentTransactionRepository.save(paymentTransaction);
    }

    public void delete(final Long id) {
        final PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        paymentTransactionRepository.delete(paymentTransaction);
    }

    private PaymentTransactionDTO mapToDTO(final PaymentTransaction paymentTransaction,
            final PaymentTransactionDTO paymentTransactionDTO) {
        paymentTransactionDTO.setId(paymentTransaction.getId());
        paymentTransactionDTO.setGatewayTxnId(paymentTransaction.getGatewayTxnId());
        paymentTransactionDTO.setTxnType(paymentTransaction.getTxnType());
        paymentTransactionDTO.setStatus(paymentTransaction.getStatus());
        paymentTransactionDTO.setRawPayload(paymentTransaction.getRawPayload());
        paymentTransactionDTO.setCreatedAt(paymentTransaction.getCreatedAt());
        paymentTransactionDTO.setPayment(paymentTransaction.getPayment() == null ? null : paymentTransaction.getPayment().getId());
        return paymentTransactionDTO;
    }

    private PaymentTransaction mapToEntity(final PaymentTransactionDTO paymentTransactionDTO,
            final PaymentTransaction paymentTransaction) {
        paymentTransaction.setGatewayTxnId(paymentTransactionDTO.getGatewayTxnId());
        paymentTransaction.setTxnType(paymentTransactionDTO.getTxnType());
        paymentTransaction.setStatus(paymentTransactionDTO.getStatus());
        paymentTransaction.setRawPayload(paymentTransactionDTO.getRawPayload());
        paymentTransaction.setCreatedAt(paymentTransactionDTO.getCreatedAt());
        final Payment payment = paymentTransactionDTO.getPayment() == null ? null : paymentRepository.findById(paymentTransactionDTO.getPayment())
                .orElseThrow(() -> new NotFoundException("payment not found"));
        paymentTransaction.setPayment(payment);
        return paymentTransaction;
    }

    @EventListener(BeforeDeletePayment.class)
    public void on(final BeforeDeletePayment event) {
        final ReferencedException referencedException = new ReferencedException();
        final PaymentTransaction paymentPaymentTransaction = paymentTransactionRepository.findFirstByPaymentId(event.getId());
        if (paymentPaymentTransaction != null) {
            referencedException.setKey("payment.paymentTransaction.payment.referenced");
            referencedException.addParam(paymentPaymentTransaction.getId());
            throw referencedException;
        }
    }

}
