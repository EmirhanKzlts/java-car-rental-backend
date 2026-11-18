package com.carrental.carrental.dtos.auth;

public record LoginResponse (

    String token,
    String tokenType,
    AuthUserResponse user
) {}
