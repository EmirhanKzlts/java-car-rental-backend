package com.carrental.carrental.dtos.auth;

import com.carrental.carrental.enums.user.UserType;

public record RegisterResponse (

        Long id,
        String firstName,
        String lastName,
        String email,
        UserType role
) {}