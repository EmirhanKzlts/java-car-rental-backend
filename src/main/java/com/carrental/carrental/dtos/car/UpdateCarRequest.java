package com.carrental.carrental.dtos.car;

import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateCarRequest {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Image is required")
    private String imageUrl;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be at least 1900")
    private Integer year;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotNull(message = "Transmission type is required")
    private TransmissionType transmissionType;

    @NotNull(message = "Mileage is required")
    @Positive(message = "Mileage must be greater than 0")
    private Integer mileage;

    @NotNull(message = "Seat count is required")
    @Positive(message = "Seat count must be greater than 0")
    private Integer seatCount;

    @NotNull(message = "Daily price is required")
    @Positive(message = "Daily price must be greater than 0")
    private BigDecimal dailyPrice;

    @NotNull(message = "Location is required")
    private Long locationId;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Status is required")
    private CarStatus status;
}
