package com.carrental.carrental.dtos.payment;

import com.carrental.carrental.enums.payment.PaymentResult;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Result can not be null")
    private PaymentResult result;
}
