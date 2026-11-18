package com.carrental.carrental.service;

import com.carrental.carrental.dtos.payment.PaymentRequest;
import com.carrental.carrental.dtos.payment.PaymentResponse;
import com.carrental.carrental.entity.Payment;
import com.carrental.carrental.entity.Reservation;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.PaymentMapper;
import com.carrental.carrental.repository.PaymentRepository;
import com.carrental.carrental.repository.ReservationRepository;
import com.carrental.carrental.repository.UserRepository;
import com.carrental.carrental.enums.payment.PaymentResult;
import com.carrental.carrental.enums.payment.PaymentStatus;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import com.carrental.carrental.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse getPayment(Long reservationId) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Reservation reservation = reservationRepository
                .findByIdAndUserId(reservationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Payment payment = paymentRepository
                .findTopByReservationIdOrderByCreatedAtDesc(reservation.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(Long reservationId, PaymentRequest request) {

        PaymentStatus paymentStatus;

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Reservation reservation = reservationRepository
                .findByIdAndUserId(reservationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Payment is only allowed for pending reservations");
        }

        if (request.getResult() == PaymentResult.SUCCESS) {
            paymentStatus = PaymentStatus.SUCCESS;
        } else {
            paymentStatus = PaymentStatus.FAILED;
        }

        Payment payment = Payment.builder()
                .amount(reservation.getTotalPrice())
                .status(paymentStatus)
                .reservation(reservation)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        if (paymentStatus == PaymentStatus.SUCCESS) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        }

        return paymentMapper.toPaymentResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentByAdmin(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Payment payment = paymentRepository
                .findTopByReservationIdOrderByCreatedAtDesc(reservation.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return paymentMapper.toPaymentResponse(payment);
    }
}
