package com.carrental.carrental.repository;

import com.carrental.carrental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u
        FROM User u
        WHERE :search IS NULL
        OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
    
    """)
    Page<User> searchUsers(
            @Param("search") String search,
            Pageable pageable
    );
}
