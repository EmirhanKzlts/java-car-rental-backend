package com.carrental.carrental.dtos.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationRequest {

    @NotNull
    private Long carId;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Long pickupLocationId;

    @NotNull
    private Long dropOffLocationId;
}
