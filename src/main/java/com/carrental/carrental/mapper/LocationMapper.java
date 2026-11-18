package com.carrental.carrental.mapper;

import com.carrental.carrental.dtos.location.LocationResponse;
import com.carrental.carrental.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationResponse toLocationResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getAddress()
        );
    }
}
