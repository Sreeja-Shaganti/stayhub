package com.neo.stayhub.service;

import com.neo.stayhub.domain.Booking;
import com.neo.stayhub.domain.Payment;
import com.neo.stayhub.events.BeforeDeleteBooking;
import com.neo.stayhub.events.BeforeDeletePayment;
import com.neo.stayhub.model.PaymentDTO;
import com.neo.stayhub.repos.BookingRepository;
import com.neo.stayhub.repos.PaymentRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher publisher;

    public PaymentService(final PaymentRepository paymentRepository,
            final BookingRepository bookingRepository, final ApplicationEventPublisher publisher) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.publisher = publisher;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    public void update(final Long id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final Long id) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeletePayment(id));
        paymentRepository.delete(payment);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setGateway(payment.getGateway());
        paymentDTO.setGatewayOrderId(payment.getGatewayOrderId());
        paymentDTO.setGatewayPaymentId(payment.getGatewayPaymentId());
        paymentDTO.setPaymentReference(payment.getPaymentReference());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setCurrency(payment.getCurrency());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setPaidAt(payment.getPaidAt());
        paymentDTO.setCreatedAt(payment.getCreatedAt());
        paymentDTO.setUpdatedAt(payment.getUpdatedAt());
        paymentDTO.setVersion(payment.getVersion());
        paymentDTO.setBooking(payment.getBooking() == null ? null : payment.getBooking().getId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setGateway(paymentDTO.getGateway());
        payment.setGatewayOrderId(paymentDTO.getGatewayOrderId());
        payment.setGatewayPaymentId(paymentDTO.getGatewayPaymentId());
        payment.setPaymentReference(paymentDTO.getPaymentReference());
        payment.setAmount(paymentDTO.getAmount());
        payment.setCurrency(paymentDTO.getCurrency());
        payment.setStatus(paymentDTO.getStatus());
        payment.setPaidAt(paymentDTO.getPaidAt());
        payment.setCreatedAt(paymentDTO.getCreatedAt());
        payment.setUpdatedAt(paymentDTO.getUpdatedAt());
        payment.setVersion(paymentDTO.getVersion());
        final Booking booking = paymentDTO.getBooking() == null ? null : bookingRepository.findById(paymentDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        payment.setBooking(booking);
        return payment;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment bookingPayment = paymentRepository.findFirstByBookingId(event.getId());
        if (bookingPayment != null) {
            referencedException.setKey("booking.payment.booking.referenced");
            referencedException.addParam(bookingPayment.getId());
            throw referencedException;
        }
    }

}
