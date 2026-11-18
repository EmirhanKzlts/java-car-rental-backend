package com.carrental.carrental.controller.admin;

import com.carrental.carrental.dtos.payment.PaymentResponse;
import com.carrental.carrental.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations/{reservationId}/payment")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<PaymentResponse> getPaymentByAdmin(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(paymentService.getPaymentByAdmin(reservationId));
    }
}
