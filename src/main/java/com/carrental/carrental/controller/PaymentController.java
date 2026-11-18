package com.carrental.carrental.controller;

import com.carrental.carrental.dtos.payment.PaymentRequest;
import com.carrental.carrental.dtos.payment.PaymentResponse;
import com.carrental.carrental.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations/{reservationId}/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.getPayment(reservationId));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable Long reservationId,
            @Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.createPayment(reservationId, paymentRequest));
    }

}
