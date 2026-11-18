package com.carrental.carrental.dtos.payment;

import com.carrental.carrental.enums.payment.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse (

    Long id,
    PaymentStatus status,
    BigDecimal amount,
    LocalDateTime createdAt
) {}
