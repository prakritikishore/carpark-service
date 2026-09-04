package com.prakritikishore.carparkservice.repository;

import com.prakritikishore.carparkservice.model.CarParkAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CarParkAvailabilityRepository extends JpaRepository<CarParkAvailability, String> {

    @Transactional
    @Modifying
    @Query("UPDATE CarParkAvailability a SET a.isStale = true")
    void markAllAsStale();
}