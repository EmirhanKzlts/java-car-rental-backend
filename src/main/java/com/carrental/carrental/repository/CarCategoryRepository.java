package com.carrental.carrental.repository;

import com.carrental.carrental.entity.CarCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {

    boolean existsByName(String name);

    @Query("""
        SELECT c.id, COUNT(car)
        FROM CarCategory c
        LEFT JOIN Car car ON car.category.id = c.id
        GROUP BY c.id
    """)
    List<Object[]> getCategoryCarCounts();

    @Query(value = """
        SELECT c
        FROM CarCategory c
        LEFT JOIN Car car ON car.category.id = c.id
        GROUP BY c.id, c.name
        ORDER BY
            CASE WHEN :sortType = 'name_asc' THEN c.name END ASC,
            CASE WHEN :sortType = 'name_desc' THEN c.name END DESC,
            CASE WHEN :sortType = 'carCount_asc' THEN COUNT(car) END ASC,
            CASE WHEN :sortType = 'carCount_desc' THEN COUNT(car) END DESC,
            c.id ASC
        """,
        countQuery = """
        SELECT COUNT(c)
        FROM CarCategory c
    """)
    Page<CarCategory> findAdminCategories(
            @Param("sortType") String sortType,
            Pageable pageable
    );
}
