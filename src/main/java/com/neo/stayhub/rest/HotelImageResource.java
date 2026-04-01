package com.neo.stayhub.rest;

import com.neo.stayhub.model.HotelImageDTO;
import com.neo.stayhub.service.HotelImageService;
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
@RequestMapping(value = "/api/hotelImages", produces = MediaType.APPLICATION_JSON_VALUE)
public class HotelImageResource {

    private final HotelImageService hotelImageService;

    public HotelImageResource(final HotelImageService hotelImageService) {
        this.hotelImageService = hotelImageService;
    }

    @GetMapping
    public ResponseEntity<List<HotelImageDTO>> getAllHotelImages() {
        return ResponseEntity.ok(hotelImageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelImageDTO> getHotelImage(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(hotelImageService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createHotelImage(
            @RequestBody @Valid final HotelImageDTO hotelImageDTO) {
        final Long createdId = hotelImageService.create(hotelImageDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateHotelImage(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HotelImageDTO hotelImageDTO) {
        hotelImageService.update(id, hotelImageDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelImage(@PathVariable(name = "id") final Long id) {
        hotelImageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
