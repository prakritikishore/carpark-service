package com.prakritikishore.carparkservice.service;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

@Component
public class Svy21Converter {

    private final CoordinateTransform transform;

    public Svy21Converter() {
        CRSFactory crsFactory = new CRSFactory();

        // Define SVY21 (EPSG:3414) with explicit parameters
        CoordinateReferenceSystem svy21Crs = crsFactory.createFromParameters(
                "EPSG:3414",
                "+proj=tmerc +lat_0=1.366666666666667 +lon_0=103.8333333333333 +k=1 +x_0=28001.642 +y_0=38744.572 +ellps=WGS84 +units=m +no_defs"
        );

        // FIX: Replaced createFromName with createFromParameters to avoid classpath lookup crash
        CoordinateReferenceSystem wgs84Crs = crsFactory.createFromParameters(
                "EPSG:4326",
                "+proj=longlat +datum=WGS84 +no_defs"
        );

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        this.transform = ctFactory.createTransform(svy21Crs, wgs84Crs);
    }

    public Point2D toWgs84(double eastingX, double northingY) {
        ProjCoordinate src = new ProjCoordinate(eastingX, northingY);
        ProjCoordinate dst = new ProjCoordinate();
        transform.transform(src, dst);
        return new Point2D(dst.y, dst.x);
    }

    public record Point2D(double latitude, double longitude) {}
}