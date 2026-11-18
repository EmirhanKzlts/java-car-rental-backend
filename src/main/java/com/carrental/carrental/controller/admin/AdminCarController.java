package com.carrental.carrental.controller.admin;

import com.carrental.carrental.dtos.car.CarResponse;
import com.carrental.carrental.dtos.car.CreateCarRequest;
import com.carrental.carrental.dtos.car.UpdateCarRequest;
import com.carrental.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cars")
public class AdminCarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponse> createCar(
            @Valid
            @RequestBody CreateCarRequest createCarRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(createCarRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Long id,
            @Valid
            @RequestBody UpdateCarRequest updateCarRequest){
        return ResponseEntity.ok(carService.updateCar(id, updateCarRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(
            @PathVariable Long id){
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
