package com.carrental.carrental.service;

import com.carrental.carrental.dtos.auth.LoginRequest;
import com.carrental.carrental.dtos.auth.LoginResponse;
import com.carrental.carrental.dtos.auth.RegisterRequest;
import com.carrental.carrental.dtos.auth.RegisterResponse;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.mapper.UserMapper;
import com.carrental.carrental.repository.UserRepository;
import com.carrental.carrental.security.JwtService;
import com.carrental.carrental.enums.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;


    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserType.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                userMapper.toAuthUserResponse(user)
        );
    }
}
