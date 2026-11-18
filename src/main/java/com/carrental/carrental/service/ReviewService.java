package com.carrental.carrental.service;

import com.carrental.carrental.dtos.review.CreateReviewRequest;
import com.carrental.carrental.dtos.review.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(Long carId, CreateReviewRequest createReviewRequest);

    List<ReviewResponse> getCarReviews(Long carId);

    void deleteReview(Long carId, Long reviewId);
}
