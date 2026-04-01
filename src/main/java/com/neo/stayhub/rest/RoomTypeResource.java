package com.neo.stayhub.rest;

import com.neo.stayhub.model.RoomTypeDTO;
import com.neo.stayhub.service.RoomTypeService;
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
@RequestMapping(value = "/api/roomTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomTypeResource {

    private final RoomTypeService roomTypeService;

    public RoomTypeResource(final RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomType(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roomTypeService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createRoomType(@RequestBody @Valid final RoomTypeDTO roomTypeDTO) {
        final Long createdId = roomTypeService.create(roomTypeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoomType(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoomTypeDTO roomTypeDTO) {
        roomTypeService.update(id, roomTypeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable(name = "id") final Long id) {
        roomTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
