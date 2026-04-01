package com.neo.stayhub.service;

import com.neo.stayhub.domain.Hotel;
import com.neo.stayhub.events.BeforeDeleteHotel;
import com.neo.stayhub.model.HotelDTO;
import com.neo.stayhub.repos.HotelRepository;
import com.neo.stayhub.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final ApplicationEventPublisher publisher;

    public HotelService(final HotelRepository hotelRepository,
            final ApplicationEventPublisher publisher) {
        this.hotelRepository = hotelRepository;
        this.publisher = publisher;
    }

    public List<HotelDTO> findAll() {
        final List<Hotel> hotels = hotelRepository.findAll(Sort.by("id"));
        return hotels.stream()
                .map(hotel -> mapToDTO(hotel, new HotelDTO()))
                .toList();
    }

    public HotelDTO get(final Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> mapToDTO(hotel, new HotelDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HotelDTO hotelDTO) {
        final Hotel hotel = new Hotel();
        mapToEntity(hotelDTO, hotel);
        return hotelRepository.save(hotel).getId();
    }

    public void update(final Long id, final HotelDTO hotelDTO) {
        final Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hotelDTO, hotel);
        hotelRepository.save(hotel);
    }

    public void delete(final Long id) {
        final Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteHotel(id));
        hotelRepository.delete(hotel);
    }

    private HotelDTO mapToDTO(final Hotel hotel, final HotelDTO hotelDTO) {
        hotelDTO.setId(hotel.getId());
        hotelDTO.setCode(hotel.getCode());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setDescription(hotel.getDescription());
        hotelDTO.setAddressLine1(hotel.getAddressLine1());
        hotelDTO.setAddressLine2(hotel.getAddressLine2());
        hotelDTO.setCity(hotel.getCity());
        hotelDTO.setState(hotel.getState());
        hotelDTO.setCountry(hotel.getCountry());
        hotelDTO.setPostalCode(hotel.getPostalCode());
        hotelDTO.setLatitude(hotel.getLatitude());
        hotelDTO.setLongitude(hotel.getLongitude());
        hotelDTO.setStarRating(hotel.getStarRating());
        hotelDTO.setStatus(hotel.getStatus());
        hotelDTO.setCreatedAt(hotel.getCreatedAt());
        hotelDTO.setUpdatedAt(hotel.getUpdatedAt());
        hotelDTO.setVersion(hotel.getVersion());
        return hotelDTO;
    }

    private Hotel mapToEntity(final HotelDTO hotelDTO, final Hotel hotel) {
        hotel.setCode(hotelDTO.getCode());
        hotel.setName(hotelDTO.getName());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setAddressLine1(hotelDTO.getAddressLine1());
        hotel.setAddressLine2(hotelDTO.getAddressLine2());
        hotel.setCity(hotelDTO.getCity());
        hotel.setState(hotelDTO.getState());
        hotel.setCountry(hotelDTO.getCountry());
        hotel.setPostalCode(hotelDTO.getPostalCode());
        hotel.setLatitude(hotelDTO.getLatitude());
        hotel.setLongitude(hotelDTO.getLongitude());
        hotel.setStarRating(hotelDTO.getStarRating());
        hotel.setStatus(hotelDTO.getStatus());
        hotel.setCreatedAt(hotelDTO.getCreatedAt());
        hotel.setUpdatedAt(hotelDTO.getUpdatedAt());
        hotel.setVersion(hotelDTO.getVersion());
        return hotel;
    }

}
