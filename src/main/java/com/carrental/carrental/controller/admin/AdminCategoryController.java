package com.carrental.carrental.controller.admin;

import com.carrental.carrental.dtos.category.CategoryCarCountResponse;
import com.carrental.carrental.dtos.category.CategoryResponse;
import com.carrental.carrental.dtos.category.CreateCategoryRequest;
import com.carrental.carrental.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAdminCategories(
            @RequestParam(required = false) String sortType,
            Pageable pageable
    ) {
        return ResponseEntity.ok(categoryService.getAdminCategories(sortType, pageable));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid
            @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(createCategoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/car-counts")
    public ResponseEntity<List<CategoryCarCountResponse>> getCategoryCarCounts() {
        return ResponseEntity.ok(categoryService.getCategoryCarCounts());
    }
}
