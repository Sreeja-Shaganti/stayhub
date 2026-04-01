package com.neo.stayhub.rest;

import com.neo.stayhub.model.BookingDTO;
import com.neo.stayhub.service.BookingService;
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
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingResource {

    private final BookingService bookingService;

    public BookingResource(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createBooking(@RequestBody @Valid final BookingDTO bookingDTO) {
        final Long createdId = bookingService.create(bookingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBooking(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BookingDTO bookingDTO) {
        bookingService.update(id, bookingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable(name = "id") final Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
