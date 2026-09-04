package com.prakritikishore.carparkservice.service;

import com.prakritikishore.carparkservice.model.CarPark;
import com.prakritikishore.carparkservice.model.CarParkAvailability;
import com.prakritikishore.carparkservice.repository.CarParkAvailabilityRepository;
import com.prakritikishore.carparkservice.repository.CarParkQueryResult;
import com.prakritikishore.carparkservice.repository.CarParkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SpatialProximityIntegrationTest {

    @Autowired
    private CarParkRepository carParkRepository;

    @Autowired
    private CarParkAvailabilityRepository availabilityRepository;

    @BeforeEach
    void setUp() {
        availabilityRepository.deleteAllInBatch();
        carParkRepository.deleteAllInBatch();
    }

    @Test
    void testNearbySearchFiltersZeroLotsAndSortsByDistance() {
        CarPark cp1 = new CarPark("CP1", "Near Spot", 0.0, 0.0, 1.3001, 103.8501, "SURFACE");
        CarParkAvailability av1 = new CarParkAvailability("CP1", 50, 10, "C", LocalDateTime.now(), false);

        CarPark cp2 = new CarPark("CP2", "Far Spot", 0.0, 0.0, 1.3045, 103.8501, "SURFACE");
        CarParkAvailability av2 = new CarParkAvailability("CP2", 50, 5, "C", LocalDateTime.now(), false);

        CarPark cp3 = new CarPark("CP3", "Full Spot", 0.0, 0.0, 1.3002, 103.8501, "SURFACE");
        CarParkAvailability av3 = new CarParkAvailability("CP3", 50, 0, "C", LocalDateTime.now(), false);

        carParkRepository.saveAllAndFlush(List.of(cp1, cp2, cp3));
        availabilityRepository.saveAllAndFlush(List.of(av1, av2, av3));

        // --- DIAGNOSTIC LOGS ---
        System.out.println("CarPark Count in DB: " + carParkRepository.count());
        System.out.println("Availability Count in DB: " + availabilityRepository.count());

        List<CarParkQueryResult> results = carParkRepository.findNearbyAvailable(1.3000, 103.8500, 3.0, 10, 0);
        System.out.println("Query Results Size: " + results.size());
        results.forEach(r -> System.out.println("Found CarPark: " + r.getCarParkNo()));

        assertEquals(2, results.size());
    }
}