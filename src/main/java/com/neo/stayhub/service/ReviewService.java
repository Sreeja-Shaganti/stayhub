package com.neo.stayhub.service;

import com.neo.stayhub.domain.Booking;
import com.neo.stayhub.domain.Hotel;
import com.neo.stayhub.domain.Review;
import com.neo.stayhub.domain.User;
import com.neo.stayhub.events.BeforeDeleteBooking;
import com.neo.stayhub.events.BeforeDeleteHotel;
import com.neo.stayhub.events.BeforeDeleteUser;
import com.neo.stayhub.model.ReviewDTO;
import com.neo.stayhub.repos.BookingRepository;
import com.neo.stayhub.repos.HotelRepository;
import com.neo.stayhub.repos.ReviewRepository;
import com.neo.stayhub.repos.UserRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final BookingRepository bookingRepository, final HotelRepository hotelRepository,
            final UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("id"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final Long id) {
        return reviewRepository.findById(id)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getId();
    }

    public void update(final Long id, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final Long id) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setBooking(review.getBooking() == null ? null : review.getBooking().getId());
        reviewDTO.setHotel(review.getHotel() == null ? null : review.getHotel().getId());
        reviewDTO.setUser(review.getUser() == null ? null : review.getUser().getId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        final Booking booking = reviewDTO.getBooking() == null ? null : bookingRepository.findById(reviewDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        review.setBooking(booking);
        final Hotel hotel = reviewDTO.getHotel() == null ? null : hotelRepository.findById(reviewDTO.getHotel())
                .orElseThrow(() -> new NotFoundException("hotel not found"));
        review.setHotel(hotel);
        final User user = reviewDTO.getUser() == null ? null : userRepository.findById(reviewDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        review.setUser(user);
        return review;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Review bookingReview = reviewRepository.findFirstByBookingId(event.getId());
        if (bookingReview != null) {
            referencedException.setKey("booking.review.booking.referenced");
            referencedException.addParam(bookingReview.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteHotel.class)
    public void on(final BeforeDeleteHotel event) {
        final ReferencedException referencedException = new ReferencedException();
        final Review hotelReview = reviewRepository.findFirstByHotelId(event.getId());
        if (hotelReview != null) {
            referencedException.setKey("hotel.review.hotel.referenced");
            referencedException.addParam(hotelReview.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Review userReview = reviewRepository.findFirstByUserId(event.getId());
        if (userReview != null) {
            referencedException.setKey("user.review.user.referenced");
            referencedException.addParam(userReview.getId());
            throw referencedException;
        }
    }

}
