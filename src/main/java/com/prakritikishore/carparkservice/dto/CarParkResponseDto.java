package com.prakritikishore.carparkservice.dto;

import java.util.List;

public record CarParkResponseDto(
        String carParkNo,
        String address,
        Double latitude,
        Double longitude,
        Double distanceKm,
        Integer totalLots,
        Integer lotsAvailable,
        String lotType,
        Boolean isStale
) {}