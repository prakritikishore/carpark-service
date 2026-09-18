package com.prakritikishore.carparkservice.repository;

public interface CarParkQueryResult {
    String getCarParkNo();
    String getAddress();
    Double getLatitude();
    Double getLongitude();
    Double getDistanceKm();
    Integer getTotalLots();
    Integer getLotsAvailable();
    String getLotType();
    Boolean getIsStale();
}