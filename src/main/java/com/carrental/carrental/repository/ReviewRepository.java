package com.carrental.carrental.repository;

import com.carrental.carrental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByReservationId(Long reservationId);

    List<Review> findByCarId(Long carId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.car.id = :carId")
    BigDecimal findAverageRatingByCarId(@Param("carId") Long carId);

    int countByCarId(Long carId);
}
