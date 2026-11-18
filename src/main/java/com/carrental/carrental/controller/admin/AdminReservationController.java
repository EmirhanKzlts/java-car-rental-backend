package com.carrental.carrental.controller.admin;

import com.carrental.carrental.dtos.reservation.AdminReservationResponse;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import com.carrental.carrental.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<AdminReservationResponse>> getAllReservations(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String sort

    ) {
        return ResponseEntity.ok(
                reservationService.getAllReservations(
                search,
                status,
                page,
                size,
                sort
            )
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservationByAdmin(
            @PathVariable Long id) {
        reservationService.cancelReservationByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
