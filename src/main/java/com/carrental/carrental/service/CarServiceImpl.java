package com.carrental.carrental.service;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.dtos.car.CreateCarRequest;
import com.carrental.carrental.dtos.car.UpdateCarRequest;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.CarCategory;
import com.carrental.carrental.entity.Location;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.CarMapper;
import com.carrental.carrental.repository.CarCategoryRepository;
import com.carrental.carrental.repository.CarRepository;
import com.carrental.carrental.repository.LocationRepository;
import com.carrental.carrental.repository.ReviewRepository;
import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final ReviewRepository reviewRepository;
    private final CarCategoryRepository carCategoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public Page<CarResponse> getAllCars(
            Long categoryId,
            Long locationId,
            String search,
            TransmissionType transmissionType,
            FuelType fuelType,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minMileage,
            Integer maxMileage,
            Integer minYear,
            Integer maxYear,
            CarStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sort
    ) {

        Sort sorting;

        if (sort == null || sort.isBlank()) {
            sorting = Sort.by("id").ascending();
        } else {
            sorting = switch (sort) {
                case "price_asc" -> Sort.by("dailyPrice").ascending();
                case "price_desc" -> Sort.by("dailyPrice").descending();

                case "mileage_asc" -> Sort.by("mileage").ascending();
                case "mileage_desc" -> Sort.by("mileage").descending();

                case "year_asc" -> Sort.by("year").ascending();
                case "year_desc" -> Sort.by("year").descending();
                default -> Sort.by("id").ascending();
            };
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        List<ReservationStatus> activeStatuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.ACTIVE
        );

        Page<Car> cars = carRepository.searchCars(
                categoryId,
                locationId,
                search,
                transmissionType,
                fuelType,
                minPrice,
                maxPrice,
                minMileage,
                maxMileage,
                minYear,
                maxYear,
                status,
                startDate,
                endDate,
                activeStatuses,
                pageable
        );

        return cars.map(this::toCarResponse);
    }

    // ADMIN

    @Override
    public CarResponse createCar(CreateCarRequest request) {

        CarCategory category = carCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found!"));

        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .imageUrl(request.getImageUrl())
                .year(request.getYear())
                .fuelType(request.getFuelType())
                .transmissionType(request.getTransmissionType())
                .mileage(request.getMileage())
                .seatCount(request.getSeatCount())
                .dailyPrice(request.getDailyPrice())
                .status(CarStatus.ACTIVE)
                .location(location)
                .category(category)
                .build();

        Car savedCar = carRepository.save(car);

        return toCarResponse(savedCar);
    }

    @Override
    public CarResponse updateCar(Long id, UpdateCarRequest updateCarRequest) {

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found!"));

        CarCategory category = carCategoryRepository.findById(updateCarRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        Location location = locationRepository.findById(updateCarRequest.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found!"));

        car.setBrand(updateCarRequest.getBrand());
        car.setModel(updateCarRequest.getModel());
        car.setImageUrl(updateCarRequest.getImageUrl());
        car.setYear(updateCarRequest.getYear());
        car.setFuelType(updateCarRequest.getFuelType());
        car.setTransmissionType(updateCarRequest.getTransmissionType());
        car.setMileage(updateCarRequest.getMileage());
        car.setSeatCount(updateCarRequest.getSeatCount());
        car.setDailyPrice(updateCarRequest.getDailyPrice());
        car.setStatus(updateCarRequest.getStatus());
        car.setLocation(location);
        car.setCategory(category);

        carRepository.save(car);
        return toCarResponse(car);
    }

    @Override
    public void deleteCar(Long id) {

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found!"));

        carRepository.delete(car);
    }

    private CarResponse toCarResponse(Car car) {

        BigDecimal averageRating =
                reviewRepository.findAverageRatingByCarId(car.getId());

        if (averageRating == null) {
            averageRating = BigDecimal.ZERO;
        }

        int reviewCount =
                reviewRepository.countByCarId(car.getId());

        return carMapper.toCarResponse(car, averageRating, reviewCount);
    }
}
