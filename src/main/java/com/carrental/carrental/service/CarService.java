package com.carrental.carrental.service;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.dtos.car.CreateCarRequest;
import com.carrental.carrental.dtos.car.UpdateCarRequest;
import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarService {

    // CUSTOMER

    Page<CarResponse> getAllCars(
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
    );

    // ADMIN

    CarResponse createCar(CreateCarRequest createCarRequest);

    CarResponse updateCar(Long id, UpdateCarRequest updateCarRequest);

    void deleteCar(Long id);
}
