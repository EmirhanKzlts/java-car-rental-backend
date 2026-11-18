package com.carrental.carrental.service;

import com.carrental.carrental.dtos.review.CreateReviewRequest;
import com.carrental.carrental.dtos.review.ReviewResponse;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Reservation;
import com.carrental.carrental.entity.Review;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.ReviewMapper;
import com.carrental.carrental.repository.CarRepository;
import com.carrental.carrental.repository.ReservationRepository;
import com.carrental.carrental.repository.ReviewRepository;
import com.carrental.carrental.repository.UserRepository;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import com.carrental.carrental.enums.user.UserType;
import com.carrental.carrental.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements  ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReview(Long carId, CreateReviewRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        Reservation reservation = reservationRepository
                .findReviewableReservation(
                        user.getId(),
                        car.getId(),
                        ReservationStatus.COMPLETED
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "You do not have a reviewable completed reservation for this car"
                        )
                );

        Review review = Review.builder()
                .user(user)
                .car(car)
                .reservation(reservation)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toReviewResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getCarReviews(Long carId) {

        carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        return reviewRepository.findByCarId(carId)
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    @Override
    public void deleteReview(Long carId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getCar().getId().equals(carId)) {
            throw new BadRequestException("Car does not belong to this review");
        }

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == UserType.ADMIN) {
            reviewRepository.delete(review);
            return;
        }

        if (!review.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You can only delete your own review");
        }

        reviewRepository.delete(review);

    }
}
