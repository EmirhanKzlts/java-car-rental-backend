package com.carrental.carrental.service;

import com.carrental.carrental.dtos.location.CreateLocationRequest;
import com.carrental.carrental.dtos.location.LocationCarCountResponse;
import com.carrental.carrental.dtos.location.LocationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {

    List<LocationResponse> getAllLocations();

    Page<LocationResponse> getAdminLocations(
            String sortType,
            Pageable pageable
    );

    LocationResponse createLocation(CreateLocationRequest createLocationRequest);

    List<LocationCarCountResponse> getLocationCarCounts();

    void deleteLocation(Long id);
}
