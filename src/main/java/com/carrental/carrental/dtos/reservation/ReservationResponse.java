package com.carrental.carrental.dtos.reservation;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.dtos.location.LocationResponse;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse (

    Long id,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal totalPrice,
    ReservationStatus status,
    CarResponse car,
    LocationResponse pickupLocation,
    LocationResponse dropOffLocation

) {}
