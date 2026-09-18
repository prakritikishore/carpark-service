package com.prakritikishore.carparkservice.dto;

import java.util.List;

public record SearchResponse(
        int resultCount,
        int page,
        int limit,
        List<CarParkResponseDto> carParks
) {}
