package com.carrental.carrental.repository;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    @Query("""
           SELECT c
           FROM Car c
           WHERE (:status IS NULL OR c.status = :status)
           AND (:categoryId IS NULL OR c.category.id = :categoryId)
           AND (:locationId IS NULL OR c.location.id = :locationId)
           AND (:search IS NULL OR
                LOWER(c.brand) LIKE LOWER(CONCAT('%', :search, '%'))
                OR
                LOWER(c.model) LIKE LOWER(CONCAT('%', :search, '%'))
           )
           AND (:transmissionType IS NULL OR c.transmissionType = :transmissionType)
           AND (:fuelType IS NULL OR c.fuelType = :fuelType)
           AND (:minPrice IS NULL OR c.dailyPrice >= :minPrice)
           AND (:maxPrice IS NULL OR c.dailyPrice <= :maxPrice)
           AND (:minMileage IS NULL OR c.mileage >= :minMileage)
           AND (:maxMileage IS NULL OR c.mileage <= :maxMileage)
           AND (:minYear IS NULL OR c.year >= :minYear)
           AND (:maxYear IS NULL OR c.year <= :maxYear)
           AND (
               :startDate IS NULL
               OR :endDate IS NULL
               OR NOT EXISTS (
                          SELECT r.id
                          FROM Reservation r
                          WHERE r.car.id = c.id
                          AND r.status IN :statuses
                          AND r.startDate < :endDate
                          AND r.endDate > :startDate
               )       
           )
           """)
    Page<Car> searchCars(
            @Param("categoryId") Long categoryId,
            @Param("locationId")  Long locationId,
            @Param("search") String search,
            @Param("transmissionType") TransmissionType transmissionType,
            @Param("fuelType") FuelType fuelType,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minMileage") Integer minMileage,
            @Param("maxMileage") Integer maxMileage,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("status") CarStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statuses") List<ReservationStatus> statuses,
            Pageable pageable
            );

    List<Car> findByStatus(CarStatus status);

    boolean existsByCategoryId(Long categoryId);

    boolean existsByLocationId(Long locationId);
}
