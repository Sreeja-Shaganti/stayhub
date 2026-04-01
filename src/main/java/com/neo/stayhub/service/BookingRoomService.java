package com.neo.stayhub.service;

import com.neo.stayhub.domain.Booking;
import com.neo.stayhub.domain.BookingRoom;
import com.neo.stayhub.domain.Room;
import com.neo.stayhub.events.BeforeDeleteBooking;
import com.neo.stayhub.events.BeforeDeleteRoom;
import com.neo.stayhub.model.BookingRoomDTO;
import com.neo.stayhub.repos.BookingRepository;
import com.neo.stayhub.repos.BookingRoomRepository;
import com.neo.stayhub.repos.RoomRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingRoomService {

    private final BookingRoomRepository bookingRoomRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingRoomService(final BookingRoomRepository bookingRoomRepository,
            final BookingRepository bookingRepository, final RoomRepository roomRepository) {
        this.bookingRoomRepository = bookingRoomRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<BookingRoomDTO> findAll() {
        final List<BookingRoom> bookingRooms = bookingRoomRepository.findAll(Sort.by("id"));
        return bookingRooms.stream()
                .map(bookingRoom -> mapToDTO(bookingRoom, new BookingRoomDTO()))
                .toList();
    }

    public BookingRoomDTO get(final Long id) {
        return bookingRoomRepository.findById(id)
                .map(bookingRoom -> mapToDTO(bookingRoom, new BookingRoomDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingRoomDTO bookingRoomDTO) {
        final BookingRoom bookingRoom = new BookingRoom();
        mapToEntity(bookingRoomDTO, bookingRoom);
        return bookingRoomRepository.save(bookingRoom).getId();
    }

    public void update(final Long id, final BookingRoomDTO bookingRoomDTO) {
        final BookingRoom bookingRoom = bookingRoomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingRoomDTO, bookingRoom);
        bookingRoomRepository.save(bookingRoom);
    }

    public void delete(final Long id) {
        final BookingRoom bookingRoom = bookingRoomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        bookingRoomRepository.delete(bookingRoom);
    }

    private BookingRoomDTO mapToDTO(final BookingRoom bookingRoom,
            final BookingRoomDTO bookingRoomDTO) {
        bookingRoomDTO.setId(bookingRoom.getId());
        bookingRoomDTO.setNightlyRate(bookingRoom.getNightlyRate());
        bookingRoomDTO.setNights(bookingRoom.getNights());
        bookingRoomDTO.setLineTotal(bookingRoom.getLineTotal());
        bookingRoomDTO.setCreatedAt(bookingRoom.getCreatedAt());
        bookingRoomDTO.setBooking(bookingRoom.getBooking() == null ? null : bookingRoom.getBooking().getId());
        bookingRoomDTO.setRoom(bookingRoom.getRoom() == null ? null : bookingRoom.getRoom().getId());
        return bookingRoomDTO;
    }

    private BookingRoom mapToEntity(final BookingRoomDTO bookingRoomDTO,
            final BookingRoom bookingRoom) {
        bookingRoom.setNightlyRate(bookingRoomDTO.getNightlyRate());
        bookingRoom.setNights(bookingRoomDTO.getNights());
        bookingRoom.setLineTotal(bookingRoomDTO.getLineTotal());
        bookingRoom.setCreatedAt(bookingRoomDTO.getCreatedAt());
        final Booking booking = bookingRoomDTO.getBooking() == null ? null : bookingRepository.findById(bookingRoomDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        bookingRoom.setBooking(booking);
        final Room room = bookingRoomDTO.getRoom() == null ? null : roomRepository.findById(bookingRoomDTO.getRoom())
                .orElseThrow(() -> new NotFoundException("room not found"));
        bookingRoom.setRoom(room);
        return bookingRoom;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final BookingRoom bookingBookingRoom = bookingRoomRepository.findFirstByBookingId(event.getId());
        if (bookingBookingRoom != null) {
            referencedException.setKey("booking.bookingRoom.booking.referenced");
            referencedException.addParam(bookingBookingRoom.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteRoom.class)
    public void on(final BeforeDeleteRoom event) {
        final ReferencedException referencedException = new ReferencedException();
        final BookingRoom roomBookingRoom = bookingRoomRepository.findFirstByRoomId(event.getId());
        if (roomBookingRoom != null) {
            referencedException.setKey("room.bookingRoom.room.referenced");
            referencedException.addParam(roomBookingRoom.getId());
            throw referencedException;
        }
    }

}
