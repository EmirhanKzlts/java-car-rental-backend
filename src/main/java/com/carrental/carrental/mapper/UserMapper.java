package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.auth.AuthUserResponse;
import com.carrental.carrental.dtos.user.AdminUserResponse;
import com.carrental.carrental.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthUserResponse toAuthUserResponse(User user) {
        return new AuthUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public AdminUserResponse toAdminUserResponse(
            User user
    ) {
        return new AdminUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
