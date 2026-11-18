package com.carrental.carrental.service;

import com.carrental.carrental.dtos.location.CreateLocationRequest;
import com.carrental.carrental.dtos.location.LocationCarCountResponse;
import com.carrental.carrental.dtos.location.LocationResponse;
import com.carrental.carrental.entity.Location;
import com.carrental.carrental.exception.BadRequestException;
import com.carrental.carrental.exception.ResourceNotFoundException;
import com.carrental.carrental.mapper.LocationMapper;
import com.carrental.carrental.repository.CarRepository;
import com.carrental.carrental.repository.LocationRepository;
import com.carrental.carrental.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<LocationResponse> getAllLocations() {

        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toLocationResponse)
                .toList();
    }

    @Override
    public Page<LocationResponse> getAdminLocations(
            String sortType,
            Pageable pageable) {
        return locationRepository
                .findAdminLocations(sortType, pageable)
                .map(locationMapper::toLocationResponse);
    }

    @Override
    public LocationResponse createLocation(CreateLocationRequest request) {

        Location location = Location.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toLocationResponse(savedLocation);
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found!"));

        boolean usedByCar = carRepository.existsByLocationId(id);

        boolean usedByPickupReservation =
                reservationRepository.existsByPickupLocationId(id);

        boolean usedByDropOffReservation =
                reservationRepository.existsByDropOffLocationId(id);

        if (usedByCar || usedByPickupReservation || usedByDropOffReservation) {
            throw new BadRequestException("Location is in use and cannot be deleted");
        }

        locationRepository.delete(location);
    }

    public List<LocationCarCountResponse> getLocationCarCounts() {

        return locationRepository.getLocationCarCounts()
                .stream()
                .map(result -> new LocationCarCountResponse(
                        (Long) result[0],
                        (Long) result[1]
                ))
                .toList();
    }
}
