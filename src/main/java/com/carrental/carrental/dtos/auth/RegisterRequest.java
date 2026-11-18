package com.carrental.carrental.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "You must enter your first name")
    private String firstName;

    @NotBlank(message = "You must enter your last name")
    private String lastName;

    @Email(message = "You must enter a valid email")
    @NotBlank(message = "You must enter an email")
    private String email;

    @NotBlank(message = "You must enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
