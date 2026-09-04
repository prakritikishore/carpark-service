package com.prakritikishore.carparkservice.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Svy21ConverterTest {

    private final Svy21Converter converter = new Svy21Converter();

    @Test
    void testSvy21ToWgs84Transformation() {
        // Known benchmark coordinates: Albert Centre Basement Carpark (ACB)
        // SVY21 Easting: 30314.7936, Northing: 31490.4987
        // Expected WGS84: Latitude ~ 1.301, Longitude ~ 103.853
        double easting = 30314.7936;
        double northing = 31490.4987;

        Svy21Converter.Point2D point = converter.toWgs84(easting, northing);

        assertEquals(1.301, point.latitude(), 0.01, "Latitude transformation out of expected bounds");
        assertEquals(103.853, point.longitude(), 0.01, "Longitude transformation out of expected bounds");
    }
}