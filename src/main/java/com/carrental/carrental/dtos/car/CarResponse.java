package com.carrental.carrental.dtos.car;

import com.carrental.carrental.dtos.category.CategoryResponse;
import com.carrental.carrental.dtos.location.LocationResponse;
import java.math.BigDecimal;

public record CarResponse (

        Long id,
        String brand,
        String model,
        String imageUrl,
        int year,
        BigDecimal dailyPrice,
        String status,
        String fuelType,
        String transmissionType,
        int mileage,
        int seatCount,
        BigDecimal averageRating,
        int reviewCount,
        CategoryResponse category,
        LocationResponse location
) {}
