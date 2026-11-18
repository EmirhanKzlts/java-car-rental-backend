package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.payment.PaymentResponse;
import com.carrental.carrental.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
