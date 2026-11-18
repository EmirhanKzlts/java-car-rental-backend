package com.carrental.carrental.service;

import com.carrental.carrental.dtos.user.AdminUserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<AdminUserResponse> getAllUsers(
            String search,
            int page,
            int size,
            String sort
    );

    AdminUserResponse getUserById(Long id);

    void deleteUser(Long id);
}
