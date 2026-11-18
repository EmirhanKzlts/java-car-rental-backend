package com.carrental.carrental.service;

import com.carrental.carrental.dtos.category.CategoryCarCountResponse;
import com.carrental.carrental.dtos.category.CategoryResponse;
import com.carrental.carrental.dtos.category.CreateCategoryRequest;
import com.carrental.carrental.entity.CarCategory;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.CarCategoryMapper;
import com.carrental.carrental.repository.CarCategoryRepository;
import com.carrental.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CarCategoryRepository carCategoryRepository;
    private final CarCategoryMapper carCategoryMapper;
    private final CarRepository carRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {

        return carCategoryRepository.findAll()
                .stream()
                .map(carCategoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public Page<CategoryResponse> getAdminCategories(
            String sortType,
            Pageable pageable
    ) {
        return carCategoryRepository
                .findAdminCategories(sortType, pageable)
                .map(carCategoryMapper::toCategoryResponse);
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        if (carCategoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category already exists");
        }

        CarCategory category = CarCategory.builder()
                .name(request.getName())
                .build();

        CarCategory savedCategory = carCategoryRepository.save(category);

        return carCategoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        CarCategory category = carCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (carRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Category is in use and cannot be deleted");
        }

        carCategoryRepository.delete(category);
    }

    public List<CategoryCarCountResponse> getCategoryCarCounts() {

        return carCategoryRepository.getCategoryCarCounts()
                .stream()
                .map(result -> new CategoryCarCountResponse(
                        (Long) result[0],
                        (Long) result[1]
                ))
                .toList();
    }


}
