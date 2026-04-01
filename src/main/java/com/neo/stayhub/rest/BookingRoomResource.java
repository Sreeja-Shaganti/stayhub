package com.neo.stayhub.rest;

import com.neo.stayhub.model.BookingRoomDTO;
import com.neo.stayhub.service.BookingRoomService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookingRooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingRoomResource {

    private final BookingRoomService bookingRoomService;

    public BookingRoomResource(final BookingRoomService bookingRoomService) {
        this.bookingRoomService = bookingRoomService;
    }

    @GetMapping
    public ResponseEntity<List<BookingRoomDTO>> getAllBookingRooms() {
        return ResponseEntity.ok(bookingRoomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingRoomDTO> getBookingRoom(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingRoomService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createBookingRoom(
            @RequestBody @Valid final BookingRoomDTO bookingRoomDTO) {
        final Long createdId = bookingRoomService.create(bookingRoomDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBookingRoom(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BookingRoomDTO bookingRoomDTO) {
        bookingRoomService.update(id, bookingRoomDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingRoom(@PathVariable(name = "id") final Long id) {
        bookingRoomService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
