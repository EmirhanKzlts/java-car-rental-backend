package com.carrental.carrental.service;

import com.carrental.carrental.dtos.payment.PaymentRequest;
import com.carrental.carrental.dtos.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse getPayment(Long reservationId);

    PaymentResponse createPayment(Long reservationId, PaymentRequest paymentRequest);

    PaymentResponse getPaymentByAdmin(Long reservationId);
}
