package com.neo.stayhub.service;

import com.neo.stayhub.domain.Amenity;
import com.neo.stayhub.events.BeforeDeleteAmenity;
import com.neo.stayhub.model.AmenityDTO;
import com.neo.stayhub.repos.AmenityRepository;
import com.neo.stayhub.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final ApplicationEventPublisher publisher;

    public AmenityService(final AmenityRepository amenityRepository,
            final ApplicationEventPublisher publisher) {
        this.amenityRepository = amenityRepository;
        this.publisher = publisher;
    }

    public List<AmenityDTO> findAll() {
        final List<Amenity> amenities = amenityRepository.findAll(Sort.by("id"));
        return amenities.stream()
                .map(amenity -> mapToDTO(amenity, new AmenityDTO()))
                .toList();
    }

    public AmenityDTO get(final Long id) {
        return amenityRepository.findById(id)
                .map(amenity -> mapToDTO(amenity, new AmenityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AmenityDTO amenityDTO) {
        final Amenity amenity = new Amenity();
        mapToEntity(amenityDTO, amenity);
        return amenityRepository.save(amenity).getId();
    }

    public void update(final Long id, final AmenityDTO amenityDTO) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(amenityDTO, amenity);
        amenityRepository.save(amenity);
    }

    public void delete(final Long id) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteAmenity(id));
        amenityRepository.delete(amenity);
    }

    private AmenityDTO mapToDTO(final Amenity amenity, final AmenityDTO amenityDTO) {
        amenityDTO.setId(amenity.getId());
        amenityDTO.setCode(amenity.getCode());
        amenityDTO.setName(amenity.getName());
        amenityDTO.setDescription(amenity.getDescription());
        amenityDTO.setCreatedAt(amenity.getCreatedAt());
        return amenityDTO;
    }

    private Amenity mapToEntity(final AmenityDTO amenityDTO, final Amenity amenity) {
        amenity.setCode(amenityDTO.getCode());
        amenity.setName(amenityDTO.getName());
        amenity.setDescription(amenityDTO.getDescription());
        amenity.setCreatedAt(amenityDTO.getCreatedAt());
        return amenity;
    }

}
