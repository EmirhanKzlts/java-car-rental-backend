package com.carrental.carrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "location")
    private List<Car> cars;

    @OneToMany(mappedBy = "pickupLocation")
    private List<Reservation> pickupReservations;

    @OneToMany(mappedBy = "dropOffLocation")
    private List<Reservation> dropOffReservations;
}
