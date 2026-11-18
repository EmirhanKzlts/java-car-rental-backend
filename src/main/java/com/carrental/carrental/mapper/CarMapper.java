package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.entity.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CarMapper {

    private final CarCategoryMapper carCategoryMapper;
    private final LocationMapper locationMapper;

    public CarResponse toCarResponse(
            Car car,
            BigDecimal averageRating,
            int reviewCount
    ) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getImageUrl(),
                car.getYear(),
                car.getDailyPrice(),
                car.getStatus().name(),
                car.getFuelType().name(),
                car.getTransmissionType().name(),
                car.getMileage(),
                car.getSeatCount(),
                averageRating,
                reviewCount,
                carCategoryMapper.toCategoryResponse(car.getCategory()),
                locationMapper.toLocationResponse(car.getLocation())
        );
    }
}
