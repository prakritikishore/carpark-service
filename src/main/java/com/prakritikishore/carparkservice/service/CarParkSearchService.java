package com.prakritikishore.carparkservice.service;

import com.prakritikishore.carparkservice.dto.CarParkResponseDto;
import com.prakritikishore.carparkservice.dto.SearchResponse;
import com.prakritikishore.carparkservice.repository.CarParkQueryResult;
import com.prakritikishore.carparkservice.repository.CarParkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarParkSearchService {

    private final CarParkRepository carParkRepository;

    public CarParkSearchService(CarParkRepository carParkRepository) {
        this.carParkRepository = carParkRepository;
    }

    public SearchResponse search(double latitude, double longitude, double radiusKm, int limit, int page) {
        int offset = page * limit;
        List<CarParkQueryResult> results = carParkRepository.findNearbyAvailable(
                latitude, longitude, radiusKm, limit, offset
        );

        List<CarParkResponseDto> dtos = results.stream().map(r -> new CarParkResponseDto(
                r.getCarParkNo(),
                r.getAddress(),
                r.getLatitude(),
                r.getLongitude(),
                Math.round(r.getDistanceKm() * 1000.0) / 1000.0, // round to 3 decimal places
                r.getTotalLots(),
                r.getLotsAvailable(),
                r.getLotType(),
                r.getIsStale()
        )).toList();

        return new SearchResponse(dtos.size(), page, limit, dtos);
    }
}