package com.carrental.carrental.controller;

import com.carrental.carrental.dtos.reservation.CreateReservationRequest;
import com.carrental.carrental.dtos.reservation.ReservationResponse;
import com.carrental.carrental.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getMyReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
           @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(createReservationRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

}
