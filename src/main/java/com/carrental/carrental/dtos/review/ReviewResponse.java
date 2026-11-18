package com.carrental.carrental.dtos.review;

import com.carrental.carrental.dtos.auth.AuthUserResponse;
import com.carrental.carrental.dtos.auth.ReviewUserResponse;

import java.time.LocalDateTime;

public record ReviewResponse (

        Long id,
        ReviewUserResponse user,
        String comment,
        Integer rating,
        LocalDateTime createdAt
) {}
