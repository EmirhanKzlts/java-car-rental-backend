package com.carrental.carrental.dtos.location;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLocationRequest {

    @NotNull(message = "You must enter a name")
    private String name;

    @NotNull(message = "You must enter an address")
    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;
}
