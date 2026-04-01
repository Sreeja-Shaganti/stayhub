package com.neo.stayhub.service;

import com.neo.stayhub.domain.RoomType;
import com.neo.stayhub.events.BeforeDeleteRoomType;
import com.neo.stayhub.model.RoomTypeDTO;
import com.neo.stayhub.repos.RoomTypeRepository;
import com.neo.stayhub.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final ApplicationEventPublisher publisher;

    public RoomTypeService(final RoomTypeRepository roomTypeRepository,
            final ApplicationEventPublisher publisher) {
        this.roomTypeRepository = roomTypeRepository;
        this.publisher = publisher;
    }

    public List<RoomTypeDTO> findAll() {
        final List<RoomType> roomTypes = roomTypeRepository.findAll(Sort.by("id"));
        return roomTypes.stream()
                .map(roomType -> mapToDTO(roomType, new RoomTypeDTO()))
                .toList();
    }

    public RoomTypeDTO get(final Long id) {
        return roomTypeRepository.findById(id)
                .map(roomType -> mapToDTO(roomType, new RoomTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoomTypeDTO roomTypeDTO) {
        final RoomType roomType = new RoomType();
        mapToEntity(roomTypeDTO, roomType);
        return roomTypeRepository.save(roomType).getId();
    }

    public void update(final Long id, final RoomTypeDTO roomTypeDTO) {
        final RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roomTypeDTO, roomType);
        roomTypeRepository.save(roomType);
    }

    public void delete(final Long id) {
        final RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteRoomType(id));
        roomTypeRepository.delete(roomType);
    }

    private RoomTypeDTO mapToDTO(final RoomType roomType, final RoomTypeDTO roomTypeDTO) {
        roomTypeDTO.setId(roomType.getId());
        roomTypeDTO.setCode(roomType.getCode());
        roomTypeDTO.setName(roomType.getName());
        roomTypeDTO.setDescription(roomType.getDescription());
        roomTypeDTO.setCapacityAdults(roomType.getCapacityAdults());
        roomTypeDTO.setCapacityChildren(roomType.getCapacityChildren());
        roomTypeDTO.setBasePrice(roomType.getBasePrice());
        roomTypeDTO.setExtraBedPrice(roomType.getExtraBedPrice());
        roomTypeDTO.setCreatedAt(roomType.getCreatedAt());
        roomTypeDTO.setUpdatedAt(roomType.getUpdatedAt());
        roomTypeDTO.setVersion(roomType.getVersion());
        return roomTypeDTO;
    }

    private RoomType mapToEntity(final RoomTypeDTO roomTypeDTO, final RoomType roomType) {
        roomType.setCode(roomTypeDTO.getCode());
        roomType.setName(roomTypeDTO.getName());
        roomType.setDescription(roomTypeDTO.getDescription());
        roomType.setCapacityAdults(roomTypeDTO.getCapacityAdults());
        roomType.setCapacityChildren(roomTypeDTO.getCapacityChildren());
        roomType.setBasePrice(roomTypeDTO.getBasePrice());
        roomType.setExtraBedPrice(roomTypeDTO.getExtraBedPrice());
        roomType.setCreatedAt(roomTypeDTO.getCreatedAt());
        roomType.setUpdatedAt(roomTypeDTO.getUpdatedAt());
        roomType.setVersion(roomTypeDTO.getVersion());
        return roomType;
    }

}
