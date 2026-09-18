package com.prakritikishore.carparkservice.repository;

import com.prakritikishore.carparkservice.model.CarPark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CarParkRepository extends JpaRepository<CarPark, String> {

    @Query(value = """
        SELECT * FROM (
            SELECT 
                c.car_park_no AS carParkNo,
                c.address AS address,
                c.latitude AS latitude,
                c.longitude AS longitude,
                COALESCE(a.total_lots, 0) AS totalLots,
                COALESCE(a.lots_available, 0) AS lotsAvailable,
                COALESCE(a.lot_type, 'C') AS lotType,
                COALESCE(a.is_stale, true) AS isStale,
                (6371.0 * acos(
                    LEAST(1.0, GREATEST(-1.0, 
                        cos(radians(:lat)) * cos(radians(c.latitude)) *
                        cos(radians(c.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(c.latitude))
                    ))
                )) AS distanceKm
            FROM carpark c
            LEFT JOIN carpark_availability a ON c.car_park_no = a.car_park_no
        ) sub
        WHERE sub.distanceKm <= :radiusKm
          AND sub.lotsAvailable > 0
        ORDER BY sub.distanceKm ASC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<CarParkQueryResult> findNearbyAvailable(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radiusKm") double radiusKm,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}