package com.neo.stayhub.service;

import com.neo.stayhub.domain.Amenity;
import com.neo.stayhub.domain.Hotel;
import com.neo.stayhub.domain.Room;
import com.neo.stayhub.domain.RoomType;
import com.neo.stayhub.events.BeforeDeleteAmenity;
import com.neo.stayhub.events.BeforeDeleteHotel;
import com.neo.stayhub.events.BeforeDeleteRoom;
import com.neo.stayhub.events.BeforeDeleteRoomType;
import com.neo.stayhub.model.RoomDTO;
import com.neo.stayhub.repos.AmenityRepository;
import com.neo.stayhub.repos.HotelRepository;
import com.neo.stayhub.repos.RoomRepository;
import com.neo.stayhub.repos.RoomTypeRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final AmenityRepository amenityRepository;
    private final ApplicationEventPublisher publisher;

    public RoomService(final RoomRepository roomRepository, final HotelRepository hotelRepository,
            final RoomTypeRepository roomTypeRepository, final AmenityRepository amenityRepository,
            final ApplicationEventPublisher publisher) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.amenityRepository = amenityRepository;
        this.publisher = publisher;
    }

    public List<RoomDTO> findAll() {
        final List<Room> rooms = roomRepository.findAll(Sort.by("id"));
        return rooms.stream()
                .map(room -> mapToDTO(room, new RoomDTO()))
                .toList();
    }

    public RoomDTO get(final Long id) {
        return roomRepository.findById(id)
                .map(room -> mapToDTO(room, new RoomDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoomDTO roomDTO) {
        final Room room = new Room();
        mapToEntity(roomDTO, room);
        return roomRepository.save(room).getId();
    }

    public void update(final Long id, final RoomDTO roomDTO) {
        final Room room = roomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roomDTO, room);
        roomRepository.save(room);
    }

    public void delete(final Long id) {
        final Room room = roomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteRoom(id));
        roomRepository.delete(room);
    }

    private RoomDTO mapToDTO(final Room room, final RoomDTO roomDTO) {
        roomDTO.setId(room.getId());
        roomDTO.setRoomNo(room.getRoomNo());
        roomDTO.setFloorNo(room.getFloorNo());
        roomDTO.setStatus(room.getStatus());
        roomDTO.setSmokingAllowed(room.getSmokingAllowed());
        roomDTO.setPriceOverride(room.getPriceOverride());
        roomDTO.setCreatedAt(room.getCreatedAt());
        roomDTO.setUpdatedAt(room.getUpdatedAt());
        roomDTO.setVersion(room.getVersion());
        roomDTO.setHotel(room.getHotel() == null ? null : room.getHotel().getId());
        roomDTO.setRoomType(room.getRoomType() == null ? null : room.getRoomType().getId());
        roomDTO.setRoomAmenityAmenities(room.getRoomAmenityAmenities().stream()
                .map(amenity -> amenity.getId())
                .toList());
        return roomDTO;
    }

    private Room mapToEntity(final RoomDTO roomDTO, final Room room) {
        room.setRoomNo(roomDTO.getRoomNo());
        room.setFloorNo(roomDTO.getFloorNo());
        room.setStatus(roomDTO.getStatus());
        room.setSmokingAllowed(roomDTO.getSmokingAllowed());
        room.setPriceOverride(roomDTO.getPriceOverride());
        room.setCreatedAt(roomDTO.getCreatedAt());
        room.setUpdatedAt(roomDTO.getUpdatedAt());
        room.setVersion(roomDTO.getVersion());
        final Hotel hotel = roomDTO.getHotel() == null ? null : hotelRepository.findById(roomDTO.getHotel())
                .orElseThrow(() -> new NotFoundException("hotel not found"));
        room.setHotel(hotel);
        final RoomType roomType = roomDTO.getRoomType() == null ? null : roomTypeRepository.findById(roomDTO.getRoomType())
                .orElseThrow(() -> new NotFoundException("roomType not found"));
        room.setRoomType(roomType);
        final List<Amenity> roomAmenityAmenities = amenityRepository.findAllById(
                roomDTO.getRoomAmenityAmenities() == null ? List.of() : roomDTO.getRoomAmenityAmenities());
        if (roomAmenityAmenities.size() != (roomDTO.getRoomAmenityAmenities() == null ? 0 : roomDTO.getRoomAmenityAmenities().size())) {
            throw new NotFoundException("one of roomAmenityAmenities not found");
        }
        room.setRoomAmenityAmenities(new HashSet<>(roomAmenityAmenities));
        return room;
    }

    @EventListener(BeforeDeleteHotel.class)
    public void on(final BeforeDeleteHotel event) {
        final ReferencedException referencedException = new ReferencedException();
        final Room hotelRoom = roomRepository.findFirstByHotelId(event.getId());
        if (hotelRoom != null) {
            referencedException.setKey("hotel.room.hotel.referenced");
            referencedException.addParam(hotelRoom.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteRoomType.class)
    public void on(final BeforeDeleteRoomType event) {
        final ReferencedException referencedException = new ReferencedException();
        final Room roomTypeRoom = roomRepository.findFirstByRoomTypeId(event.getId());
        if (roomTypeRoom != null) {
            referencedException.setKey("roomType.room.roomType.referenced");
            referencedException.addParam(roomTypeRoom.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteAmenity.class)
    public void on(final BeforeDeleteAmenity event) {
        // remove many-to-many relations at owning side
        roomRepository.findAllByRoomAmenityAmenitiesId(event.getId()).forEach(room ->
                room.getRoomAmenityAmenities().removeIf(amenity -> amenity.getId().equals(event.getId())));
    }

}
