package com.carrental.carrental.repository;

import com.carrental.carrental.entity.Reservation;
import com.carrental.carrental.enums.reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.car.id = :carId
            AND r.status IN :statuses
            AND r.startDate < :endDate
            AND r.endDate > :startDate
            """)
    boolean existsOverlappingReservation(
            Long carId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<ReservationStatus> statuses
    );

    boolean existsByUserIdAndCarIdAndStartDateAndEndDateAndStatus(
            Long userId,
            Long carId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            ReservationStatus status
    );

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.user.id = :userId
            AND r.car.id = :carId
            AND r.status = :status
            AND NOT EXISTS (
                SELECT rev.id FROM Review rev
                WHERE rev.reservation.id = r.id        
            )
            ORDER BY r.endDate DESC
            """)
    Optional<Reservation> findReviewableReservation(
            Long userId,
            Long carId,
            ReservationStatus status
    );

    boolean existsByPickupLocationId(Long locationId);

    boolean existsByDropOffLocationId(Long locationId);

    @Query("""
        SELECT r
        FROM Reservation r
        WHERE (
            :search IS NULL
            OR LOWER(r.user.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(r.user.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(r.car.brand) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(r.car.model) LIKE LOWER(CONCAT('%', :search, '%'))    
        )
        AND (
            :status IS NULL
            OR r.status = :status
        )
        """)
    Page<Reservation> searchAdminReservations(
            @Param("search") String search,
            @Param("status") ReservationStatus status,
            Pageable pageable
    );

}
