package com.prakritikishore.carparkservice.service;


import com.prakritikishore.carparkservice.dto.GovernmentApiResponse;
import com.prakritikishore.carparkservice.model.CarParkAvailability;
import com.prakritikishore.carparkservice.repository.CarParkAvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityPollerService {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityPollerService.class);

    private final RestTemplate restTemplate;
    private final CarParkAvailabilityRepository availabilityRepository;

    @Value("${carpark.availability-url}")
    private String availabilityUrl;

    public AvailabilityPollerService(RestTemplate restTemplate, CarParkAvailabilityRepository availabilityRepository) {
        this.restTemplate = restTemplate;
        this.availabilityRepository = availabilityRepository;
    }

    @Scheduled(fixedRateString = "${carpark.poll-rate-ms:60000}")
    public void fetchAvailability() {
        log.info("Polling car park availability from {}", availabilityUrl);
        try {
            GovernmentApiResponse response = restTemplate.getForObject(availabilityUrl, GovernmentApiResponse.class);

            if (response == null || response.items() == null || response.items().isEmpty()) {
                log.warn("Received empty response from availability API. Graceful degradation triggered.");
                availabilityRepository.markAllAsStale();
                return;
            }

            List<CarParkAvailability> updates = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (GovernmentApiResponse.Item item : response.items()) {
                if (item.carParkData() == null) continue;
                for (GovernmentApiResponse.CarParkData cpData : item.carParkData()) {
                    if (cpData.carParkInfo() == null || cpData.carParkInfo().isEmpty()) continue;

                    GovernmentApiResponse.CarParkInfo info = cpData.carParkInfo().get(0);
                    int total = parseSafely(info.totalLots());
                    int avail = parseSafely(info.lotsAvailable());

                    updates.add(new CarParkAvailability(
                            cpData.carParkNumber(), total, avail, info.lotType(), now, false
                    ));
                }
            }

            availabilityRepository.saveAll(updates);
            log.info("Successfully updated availability for {} car parks.", updates.size());

        } catch (Exception e) {
            log.error("Failed to fetch availability data: {}. Marking existing data as stale.", e.getMessage());
            availabilityRepository.markAllAsStale();
        }
    }

    private int parseSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
