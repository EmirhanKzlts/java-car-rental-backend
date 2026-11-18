package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.reservation.AdminReservationResponse;
import com.carrental.carrental.dtos.reservation.ReservationResponse;
import com.carrental.carrental.dtos.user.AdminUserResponse;
import com.carrental.carrental.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final CarMapper carMapper;
    private final LocationMapper locationMapper;
    private final UserMapper userMapper;

    public ReservationResponse toReservationResponse(
            Reservation reservation,
            BigDecimal averageRating,
            int reviewCount
            ) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                carMapper.toCarResponse(reservation.getCar(), averageRating, reviewCount),
                locationMapper.toLocationResponse(reservation.getPickupLocation()),
                locationMapper.toLocationResponse(reservation.getDropOffLocation())
        );
    }

    public AdminReservationResponse toAdminReservationResponse(
            Reservation reservation,
            BigDecimal averageRating,
            int reviewCount
            ) {
        return new AdminReservationResponse(
                reservation.getId(),
                userMapper.toAdminUserResponse(reservation.getUser()),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                carMapper.toCarResponse(reservation.getCar(), averageRating, reviewCount),
                locationMapper.toLocationResponse(reservation.getPickupLocation()),
                locationMapper.toLocationResponse(reservation.getDropOffLocation())
        );
    }
}
