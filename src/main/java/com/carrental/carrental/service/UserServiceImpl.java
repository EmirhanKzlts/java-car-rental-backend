package com.carrental.carrental.service;

import com.carrental.carrental.dtos.user.AdminUserResponse;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.UserMapper;
import com.carrental.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<AdminUserResponse> getAllUsers(
            String search,
            int page,
            int size,
            String sort
    ) {
        Sort sorting;

        if (sort == null || sort.isBlank()) {
            sorting = Sort.by("id").ascending();
        } else {
            sorting = switch (sort) {
                case "name_asc" -> Sort.by("firstName").ascending();
                case "name_desc" -> Sort.by("firstName").descending();
                case "surname_asc" -> Sort.by("lastName").ascending();
                case "surname_desc" -> Sort.by("lastName").descending();
                default -> Sort.by("id").ascending();
            };
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<User> users = userRepository.searchUsers(
                search,
                pageable
        );
        return users.map(userMapper::toAdminUserResponse);
    }

    @Override
    public AdminUserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.toAdminUserResponse(user);
    }


    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        userRepository.delete(user);

    }
}
