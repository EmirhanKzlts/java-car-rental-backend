package com.carrental.carrental.dtos.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotNull(message = "You must enter a name")
    private String name;
}
