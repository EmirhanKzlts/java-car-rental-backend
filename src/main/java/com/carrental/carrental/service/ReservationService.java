package com.carrental.carrental.service;

import com.carrental.carrental.dtos.reservation.AdminReservationResponse;
import com.carrental.carrental.dtos.reservation.CreateReservationRequest;
import com.carrental.carrental.dtos.reservation.ReservationResponse;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReservationService {

    ReservationResponse createReservation(CreateReservationRequest createReservationRequest);

    List<ReservationResponse> getMyReservations();

    ReservationResponse getReservationById(Long id);

    void cancelReservation(Long id);

    // ADMIN

    Page<AdminReservationResponse> getAllReservations(
            String search,
            ReservationStatus status,
            int page,
            int size,
            String sort
    );

    void cancelReservationByAdmin(Long id);


}
