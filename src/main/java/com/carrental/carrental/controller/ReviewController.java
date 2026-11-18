package com.carrental.carrental.controller;

import com.carrental.carrental.dtos.review.CreateReviewRequest;
import com.carrental.carrental.dtos.review.ReviewResponse;
import com.carrental.carrental.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars/{carId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(@PathVariable Long carId) {
        return ResponseEntity.ok(reviewService.getCarReviews(carId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long carId,
            @Valid @RequestBody CreateReviewRequest createReviewRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(carId, createReviewRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long carId,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(carId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
