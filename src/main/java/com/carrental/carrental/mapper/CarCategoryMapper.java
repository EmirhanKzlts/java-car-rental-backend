package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.category.CategoryResponse;
import com.carrental.carrental.entity.CarCategory;
import org.springframework.stereotype.Component;

@Component
public class CarCategoryMapper {

    public CategoryResponse toCategoryResponse(CarCategory carCategory) {
        return new CategoryResponse(
                carCategory.getId(),
                carCategory.getName()
        );
    }
}
