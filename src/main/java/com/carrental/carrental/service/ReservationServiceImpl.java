package com.carrental.carrental.service;

import com.carrental.carrental.dtos.reservation.AdminReservationResponse;
import com.carrental.carrental.dtos.reservation.CreateReservationRequest;
import com.carrental.carrental.dtos.reservation.ReservationResponse;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Location;
import com.carrental.carrental.entity.Reservation;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.ReservationMapper;
import com.carrental.carrental.repository.*;
import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import com.carrental.carrental.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final LocationRepository locationRepository;
    private final ReservationMapper reservationMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public ReservationResponse createReservation(CreateReservationRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        if (car.getStatus() != CarStatus.ACTIVE) {
            throw new BadRequestException("Car is not available");
        }

        if (!request.getStartDate().isBefore(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }

        if (request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start date cannot be in the past");
        }

        if (reservationRepository.existsByUserIdAndCarIdAndStartDateAndEndDateAndStatus(
                user.getId(),
                car.getId(),
                request.getStartDate(),
                request.getEndDate(),
                ReservationStatus.PENDING
        )) {
            throw new BadRequestException("You already have a pending reservation for this car and dates");
        }

        List<ReservationStatus> blockingStatuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.ACTIVE
        );

        if (reservationRepository.existsOverlappingReservation(
                car.getId(),
                request.getStartDate(),
                request.getEndDate(),
                blockingStatuses
        )) {
            throw new BadRequestException("Car is already reserved for the selected dates");
        }

        Location pickupLocation = locationRepository
                .findById(request.getPickupLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found"));

        Location dropOffLocation = locationRepository
                .findById(request.getDropOffLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Drop-off location not found"));

        long days = Math.max(
                    1,
                    ChronoUnit.DAYS.between(
                            request.getStartDate().toLocalDate(),
                            request.getEndDate().toLocalDate()
            )
        );

        BigDecimal totalPrice = car.getDailyPrice().multiply(BigDecimal.valueOf(days));

        Reservation reservation = Reservation.builder()
                .user(user)
                .car(car)
                .pickupLocation(pickupLocation)
                .dropOffLocation(dropOffLocation)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(totalPrice)
                .status(ReservationStatus.PENDING)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toReservationResponse(
                savedReservation,
                getAverageRating(car.getId()),
                getReviewCount(car.getId())
                );
    }

    @Override
    public List<ReservationResponse> getMyReservations() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return reservationRepository.findByUserId(user.getId())
                .stream()
                .map(reservation -> reservationMapper.toReservationResponse(
                        reservation,
                        getAverageRating(reservation.getCar().getId()),
                        getReviewCount(reservation.getCar().getId())
                ))
                .toList();
    }

    @Override
    public ReservationResponse getReservationById(Long id) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Reservation reservation = reservationRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        return reservationMapper.toReservationResponse(
                reservation,
                getAverageRating(reservation.getCar().getId()),
                getReviewCount(reservation.getCar().getId())
        );
    }

    @Override
    public void cancelReservation(Long id) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Reservation reservation = reservationRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->  new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("Reservation already cancelled");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING &&
            reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Reservation cannot be cancelled");
        }

        LocalDateTime now = LocalDateTime.now();

        long hoursUntilStart = ChronoUnit.HOURS.between(
                now,
                reservation.getStartDate()
        );

        if (hoursUntilStart < 8) {
            throw new BadRequestException("Reservation cannot be cancelled less than 8 hours before start");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);

    }

    @Override
    public Page<AdminReservationResponse> getAllReservations(
            String search,
            ReservationStatus status,
            int page,
            int size,
            String sort
    ) {

        Sort sorting;

        if (sort == null || sort.isBlank()) {
            sorting = Sort.by("id").ascending();
        } else {
            sorting = switch (sort) {
                case "startDate_asc" ->
                        Sort.by("startDate").ascending();

                case "startDate_desc" ->
                        Sort.by("startDate").descending();

                case "endDate_asc" ->
                        Sort.by("endDate").ascending();

                case "endDate_desc" ->
                        Sort.by("endDate").descending();

                case "price_asc" ->
                        Sort.by("totalPrice").ascending();

                case "price_desc" ->
                        Sort.by("totalPrice").descending();

                default ->
                        Sort.by("id").descending();
            };
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Reservation> reservations =
                reservationRepository.searchAdminReservations(
                        search,
                        status,
                        pageable
                );

        return reservations.map(reservation ->
                reservationMapper.toAdminReservationResponse(
                        reservation,
                        getAverageRating(reservation.getCar().getId()),
                        getReviewCount(reservation.getCar().getId())
                )
        );
    }

    @Override
    public void cancelReservationByAdmin(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING &&
                reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Reservation cannot be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }

    private BigDecimal getAverageRating(Long carId) {
        BigDecimal averageRating =
                reviewRepository.findAverageRatingByCarId(carId);

        return averageRating != null
                ? averageRating
                : BigDecimal.ZERO;
    }

    private int getReviewCount(Long carId) {
        return reviewRepository.countByCarId(carId);
    }
}
