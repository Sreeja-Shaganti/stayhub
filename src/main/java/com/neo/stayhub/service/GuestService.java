package com.neo.stayhub.service;

import com.neo.stayhub.domain.Booking;
import com.neo.stayhub.domain.Guest;
import com.neo.stayhub.events.BeforeDeleteBooking;
import com.neo.stayhub.model.GuestDTO;
import com.neo.stayhub.repos.BookingRepository;
import com.neo.stayhub.repos.GuestRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    public GuestService(final GuestRepository guestRepository,
            final BookingRepository bookingRepository) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<GuestDTO> findAll() {
        final List<Guest> guests = guestRepository.findAll(Sort.by("id"));
        return guests.stream()
                .map(guest -> mapToDTO(guest, new GuestDTO()))
                .toList();
    }

    public GuestDTO get(final Long id) {
        return guestRepository.findById(id)
                .map(guest -> mapToDTO(guest, new GuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GuestDTO guestDTO) {
        final Guest guest = new Guest();
        mapToEntity(guestDTO, guest);
        return guestRepository.save(guest).getId();
    }

    public void update(final Long id, final GuestDTO guestDTO) {
        final Guest guest = guestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(guestDTO, guest);
        guestRepository.save(guest);
    }

    public void delete(final Long id) {
        final Guest guest = guestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        guestRepository.delete(guest);
    }

    private GuestDTO mapToDTO(final Guest guest, final GuestDTO guestDTO) {
        guestDTO.setId(guest.getId());
        guestDTO.setFullName(guest.getFullName());
        guestDTO.setEmail(guest.getEmail());
        guestDTO.setPhone(guest.getPhone());
        guestDTO.setIdProofType(guest.getIdProofType());
        guestDTO.setIdProofNumber(guest.getIdProofNumber());
        guestDTO.setIsPrimaryGuest(guest.getIsPrimaryGuest());
        guestDTO.setCreatedAt(guest.getCreatedAt());
        guestDTO.setBooking(guest.getBooking() == null ? null : guest.getBooking().getId());
        return guestDTO;
    }

    private Guest mapToEntity(final GuestDTO guestDTO, final Guest guest) {
        guest.setFullName(guestDTO.getFullName());
        guest.setEmail(guestDTO.getEmail());
        guest.setPhone(guestDTO.getPhone());
        guest.setIdProofType(guestDTO.getIdProofType());
        guest.setIdProofNumber(guestDTO.getIdProofNumber());
        guest.setIsPrimaryGuest(guestDTO.getIsPrimaryGuest());
        guest.setCreatedAt(guestDTO.getCreatedAt());
        final Booking booking = guestDTO.getBooking() == null ? null : bookingRepository.findById(guestDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        guest.setBooking(booking);
        return guest;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Guest bookingGuest = guestRepository.findFirstByBookingId(event.getId());
        if (bookingGuest != null) {
            referencedException.setKey("booking.guest.booking.referenced");
            referencedException.addParam(bookingGuest.getId());
            throw referencedException;
        }
    }

}
