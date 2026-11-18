package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.auth.ReviewUserResponse;
import com.carrental.carrental.dtos.review.ReviewResponse;
import com.carrental.carrental.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                new ReviewUserResponse(
                        review.getUser().getId(),
                        review.getUser().getFirstName(),
                        review.getUser().getLastName()
                ),
                review.getComment(),
                review.getRating(),
                review.getCreatedAt()
        );
    }
}
