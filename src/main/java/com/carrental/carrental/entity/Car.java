package com.carrental.carrental.entity;

import com.carrental.carrental.enums.car.CarStatus;
import com.carrental.carrental.enums.car.FuelType;
import com.carrental.carrental.enums.car.TransmissionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private int seatCount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmissionType;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CarCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "car")
    private List<Review> reviews;
}
