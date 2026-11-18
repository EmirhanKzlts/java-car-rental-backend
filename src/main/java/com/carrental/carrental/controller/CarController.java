package com.carrental.carrental.controller;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import com.carrental.carrental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<Page<CarResponse>> getAllCars(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TransmissionType transmissionType,
            @RequestParam(required = false) FuelType fuelType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minMileage,
            @RequestParam(required = false) Integer maxMileage,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) CarStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String sort
    ) {
        return ResponseEntity.ok(
                carService.getAllCars(
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
                        page,
                        size,
                        sort
                )
        );
    }
}
