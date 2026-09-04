package com.prakritikishore.carparkservice.controller;

import com.prakritikishore.carparkservice.dto.SearchResponse;
import com.prakritikishore.carparkservice.service.CarParkSearchService;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carparks")
@Validated
public class CarParkController {

    private final CarParkSearchService searchService;

    public CarParkController(CarParkSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/nearby")
    public ResponseEntity<SearchResponse> getNearby(
            @RequestParam @Min(value = -90, message = "Latitude must be >= -90") @Max(value = 90, message = "Latitude must be <= 90") double latitude,
            @RequestParam @Min(value = -180, message = "Longitude must be >= -180") @Max(value = 180, message = "Longitude must be <= 180") double longitude,
            @RequestParam(defaultValue = "1.0") @DecimalMin("0.1") @DecimalMax("20.0") double radiusKm,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
            @RequestParam(defaultValue = "0") @Min(0) int page) {

        SearchResponse response = searchService.search(latitude, longitude, radiusKm, limit, page);
        return ResponseEntity.ok(response);
    }
}
