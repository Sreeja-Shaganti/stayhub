package com.neo.stayhub.service;

import com.neo.stayhub.domain.Booking;
import com.neo.stayhub.domain.Hotel;
import com.neo.stayhub.domain.User;
import com.neo.stayhub.events.BeforeDeleteBooking;
import com.neo.stayhub.events.BeforeDeleteHotel;
import com.neo.stayhub.events.BeforeDeleteUser;
import com.neo.stayhub.model.BookingDTO;
import com.neo.stayhub.repos.BookingRepository;
import com.neo.stayhub.repos.HotelRepository;
import com.neo.stayhub.repos.UserRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ApplicationEventPublisher publisher;

    public BookingService(final BookingRepository bookingRepository,
            final UserRepository userRepository, final HotelRepository hotelRepository,
            final ApplicationEventPublisher publisher) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.publisher = publisher;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("id"));
        return bookings.stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final Long id) {
        return bookingRepository.findById(id)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getId();
    }

    public void update(final Long id, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final Long id) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBooking(id));
        bookingRepository.delete(booking);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingNo(booking.getBookingNo());
        bookingDTO.setCheckIn(booking.getCheckIn());
        bookingDTO.setCheckOut(booking.getCheckOut());
        bookingDTO.setAdults(booking.getAdults());
        bookingDTO.setChildren(booking.getChildren());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setCurrency(booking.getCurrency());
        bookingDTO.setSubtotal(booking.getSubtotal());
        bookingDTO.setTaxAmount(booking.getTaxAmount());
        bookingDTO.setDiscountAmount(booking.getDiscountAmount());
        bookingDTO.setTotalAmount(booking.getTotalAmount());
        bookingDTO.setSpecialRequests(booking.getSpecialRequests());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setUpdatedAt(booking.getUpdatedAt());
        bookingDTO.setVersion(booking.getVersion());
        bookingDTO.setUser(booking.getUser() == null ? null : booking.getUser().getId());
        bookingDTO.setHotel(booking.getHotel() == null ? null : booking.getHotel().getId());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setBookingNo(bookingDTO.getBookingNo());
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setAdults(bookingDTO.getAdults());
        booking.setChildren(bookingDTO.getChildren());
        booking.setStatus(bookingDTO.getStatus());
        booking.setCurrency(bookingDTO.getCurrency());
        booking.setSubtotal(bookingDTO.getSubtotal());
        booking.setTaxAmount(bookingDTO.getTaxAmount());
        booking.setDiscountAmount(bookingDTO.getDiscountAmount());
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setSpecialRequests(bookingDTO.getSpecialRequests());
        booking.setCreatedAt(bookingDTO.getCreatedAt());
        booking.setUpdatedAt(bookingDTO.getUpdatedAt());
        booking.setVersion(bookingDTO.getVersion());
        final User user = bookingDTO.getUser() == null ? null : userRepository.findById(bookingDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        booking.setUser(user);
        final Hotel hotel = bookingDTO.getHotel() == null ? null : hotelRepository.findById(bookingDTO.getHotel())
                .orElseThrow(() -> new NotFoundException("hotel not found"));
        booking.setHotel(hotel);
        return booking;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking userBooking = bookingRepository.findFirstByUserId(event.getId());
        if (userBooking != null) {
            referencedException.setKey("user.booking.user.referenced");
            referencedException.addParam(userBooking.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteHotel.class)
    public void on(final BeforeDeleteHotel event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking hotelBooking = bookingRepository.findFirstByHotelId(event.getId());
        if (hotelBooking != null) {
            referencedException.setKey("hotel.booking.hotel.referenced");
            referencedException.addParam(hotelBooking.getId());
            throw referencedException;
        }
    }

}
