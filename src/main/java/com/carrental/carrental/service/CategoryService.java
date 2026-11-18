package com.carrental.carrental.service;

import com.carrental.carrental.dtos.category.CategoryCarCountResponse;
import com.carrental.carrental.dtos.category.CategoryResponse;
import com.carrental.carrental.dtos.category.CreateCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    Page<CategoryResponse> getAdminCategories(
            String sortType,
            Pageable pageable
    );

    CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

    List<CategoryCarCountResponse> getCategoryCarCounts();

    void deleteCategory(Long id);

}
