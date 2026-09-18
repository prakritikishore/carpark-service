package com.prakritikishore.carparkservice.service;


import com.opencsv.CSVReader;
import com.prakritikishore.carparkservice.model.CarPark;
import com.prakritikishore.carparkservice.repository.CarParkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationService.class);

    private final CarParkRepository carParkRepository;
    private final Svy21Converter svy21Converter;
    private final ResourceLoader resourceLoader;

    @Value("${carpark.csv-path}")
    private String csvPath;

    public DataInitializationService(CarParkRepository carParkRepository,
                                     Svy21Converter svy21Converter,
                                     ResourceLoader resourceLoader) {
        this.carParkRepository = carParkRepository;
        this.svy21Converter = svy21Converter;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        if (carParkRepository.count() > 0) {
            log.info("Car park static dataset already initialized.");
            return;
        }

        log.info("Loading car park static dataset from {}", csvPath);
        Resource resource = resourceLoader.getResource(csvPath);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] header = reader.readNext(); // Skip header
            List<CarPark> carParks = new ArrayList<>();
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length < 5) continue;

                String carParkNo = line[0].trim();
                String address = line[1].trim();
                double xCoord = Double.parseDouble(line[2].trim());
                double yCoord = Double.parseDouble(line[3].trim());
                String carParkType = line[4].trim();

                Svy21Converter.Point2D point = svy21Converter.toWgs84(xCoord, yCoord);

                carParks.add(new CarPark(
                        carParkNo, address, xCoord, yCoord,
                        point.latitude(), point.longitude(), carParkType
                ));
            }

            carParkRepository.saveAll(carParks);
            log.info("Successfully ingested {} car parks.", carParks.size());
        }
    }
}
