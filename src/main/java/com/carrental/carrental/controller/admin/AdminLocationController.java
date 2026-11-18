package com.carrental.carrental.controller.admin;

import com.carrental.carrental.dtos.location.CreateLocationRequest;
import com.carrental.carrental.dtos.location.LocationCarCountResponse;
import com.carrental.carrental.dtos.location.LocationResponse;
import com.carrental.carrental.service.LocationService;
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
@RequestMapping("/api/admin/locations")
public class AdminLocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<Page<LocationResponse>> getAdminLocations(
            @RequestParam(required = false) String sortType,
            Pageable pageable
    ) {
        return ResponseEntity.ok(locationService.getAdminLocations(sortType, pageable));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody CreateLocationRequest createLocationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(createLocationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/car-counts")
    public ResponseEntity<List<LocationCarCountResponse>> getLocationCarCounts() {
        return ResponseEntity.ok(locationService.getLocationCarCounts());
    }
}
