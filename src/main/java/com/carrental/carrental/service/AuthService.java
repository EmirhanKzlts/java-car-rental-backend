package com.carrental.carrental.service;

import com.carrental.carrental.dtos.auth.LoginRequest;
import com.carrental.carrental.dtos.auth.LoginResponse;
import com.carrental.carrental.dtos.auth.RegisterRequest;
import com.carrental.carrental.dtos.auth.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

}
