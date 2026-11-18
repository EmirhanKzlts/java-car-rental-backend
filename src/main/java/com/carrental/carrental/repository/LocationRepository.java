package com.carrental.carrental.repository;

import com.carrental.carrental.entity.CarCategory;
import com.carrental.carrental.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

    boolean existsByName(String name);

    @Query("""
    SELECT l.id, COUNT(c)
    FROM Location l
    LEFT JOIN Car c ON c.location.id = l.id
    GROUP BY l.id
""")
    List<Object[]> getLocationCarCounts();

    @Query(value = """
        SELECT l
        FROM Location l
        LEFT JOIN Car car ON car.location.id = l.id
        GROUP BY l.id, l.name, l.address
        ORDER BY
            CASE WHEN :sortType = 'name_asc' THEN l.name END ASC,
            CASE WHEN :sortType = 'name_desc' THEN l.name END DESC,
            CASE WHEN :sortType = 'address_asc' THEN l.address END ASC,
            CASE WHEN :sortType = 'address_desc' THEN l.address END DESC,
            CASE WHEN :sortType = 'carCount_asc' THEN COUNT(car) END ASC,
            CASE WHEN :sortType = 'carCount_desc' THEN COUNT(car) END DESC,
            l.id ASC
        """,
            countQuery = """
        SELECT COUNT(l)
        FROM Location l
    """)
    Page<Location> findAdminLocations(
            @Param("sortType") String sortType,
            Pageable pageable
    );
}
