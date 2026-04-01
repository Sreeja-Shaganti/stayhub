package com.neo.stayhub.service;

import com.neo.stayhub.domain.Hotel;
import com.neo.stayhub.domain.HotelImage;
import com.neo.stayhub.events.BeforeDeleteHotel;
import com.neo.stayhub.model.HotelImageDTO;
import com.neo.stayhub.repos.HotelImageRepository;
import com.neo.stayhub.repos.HotelRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HotelImageService {

    private final HotelImageRepository hotelImageRepository;
    private final HotelRepository hotelRepository;

    public HotelImageService(final HotelImageRepository hotelImageRepository,
            final HotelRepository hotelRepository) {
        this.hotelImageRepository = hotelImageRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<HotelImageDTO> findAll() {
        final List<HotelImage> hotelImages = hotelImageRepository.findAll(Sort.by("id"));
        return hotelImages.stream()
                .map(hotelImage -> mapToDTO(hotelImage, new HotelImageDTO()))
                .toList();
    }

    public HotelImageDTO get(final Long id) {
        return hotelImageRepository.findById(id)
                .map(hotelImage -> mapToDTO(hotelImage, new HotelImageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HotelImageDTO hotelImageDTO) {
        final HotelImage hotelImage = new HotelImage();
        mapToEntity(hotelImageDTO, hotelImage);
        return hotelImageRepository.save(hotelImage).getId();
    }

    public void update(final Long id, final HotelImageDTO hotelImageDTO) {
        final HotelImage hotelImage = hotelImageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hotelImageDTO, hotelImage);
        hotelImageRepository.save(hotelImage);
    }

    public void delete(final Long id) {
        final HotelImage hotelImage = hotelImageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        hotelImageRepository.delete(hotelImage);
    }

    private HotelImageDTO mapToDTO(final HotelImage hotelImage, final HotelImageDTO hotelImageDTO) {
        hotelImageDTO.setId(hotelImage.getId());
        hotelImageDTO.setImageUrl(hotelImage.getImageUrl());
        hotelImageDTO.setAltText(hotelImage.getAltText());
        hotelImageDTO.setIsCover(hotelImage.getIsCover());
        hotelImageDTO.setDisplayOrder(hotelImage.getDisplayOrder());
        hotelImageDTO.setCreatedAt(hotelImage.getCreatedAt());
        hotelImageDTO.setHotel(hotelImage.getHotel() == null ? null : hotelImage.getHotel().getId());
        return hotelImageDTO;
    }

    private HotelImage mapToEntity(final HotelImageDTO hotelImageDTO, final HotelImage hotelImage) {
        hotelImage.setImageUrl(hotelImageDTO.getImageUrl());
        hotelImage.setAltText(hotelImageDTO.getAltText());
        hotelImage.setIsCover(hotelImageDTO.getIsCover());
        hotelImage.setDisplayOrder(hotelImageDTO.getDisplayOrder());
        hotelImage.setCreatedAt(hotelImageDTO.getCreatedAt());
        final Hotel hotel = hotelImageDTO.getHotel() == null ? null : hotelRepository.findById(hotelImageDTO.getHotel())
                .orElseThrow(() -> new NotFoundException("hotel not found"));
        hotelImage.setHotel(hotel);
        return hotelImage;
    }

    @EventListener(BeforeDeleteHotel.class)
    public void on(final BeforeDeleteHotel event) {
        final ReferencedException referencedException = new ReferencedException();
        final HotelImage hotelHotelImage = hotelImageRepository.findFirstByHotelId(event.getId());
        if (hotelHotelImage != null) {
            referencedException.setKey("hotel.hotelImage.hotel.referenced");
            referencedException.addParam(hotelHotelImage.getId());
            throw referencedException;
        }
    }

}
