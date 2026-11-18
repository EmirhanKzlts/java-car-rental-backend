package com.carrental.carrental.dtos.user;

import com.carrental.carrental.enums.user.UserType;

import java.time.LocalDateTime;

public record AdminUserResponse (

    Long id,
    String firstName,
    String lastName,
    String email,
    UserType role,
    LocalDateTime createdAt

) {}
