package com.prakritikishore.carparkservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carpark_availability")
public class CarParkAvailability {

    @Id
    @Column(name = "car_park_no", nullable = false, length = 20)
    private String carParkNo;

    @Column(name = "total_lots")
    private Integer totalLots;

    @Column(name = "lots_available")
    private Integer lotsAvailable;

    @Column(name = "lot_type")
    private String lotType;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "is_stale")
    private Boolean isStale = false;

    public CarParkAvailability() {}

    public CarParkAvailability(String carParkNo, Integer totalLots, Integer lotsAvailable, String lotType, LocalDateTime lastUpdated, Boolean isStale) {
        this.carParkNo = carParkNo;
        this.totalLots = totalLots;
        this.lotsAvailable = lotsAvailable;
        this.lotType = lotType;
        this.lastUpdated = lastUpdated;
        this.isStale = isStale;
    }

    public String getCarParkNo() { return carParkNo; }
    public void setCarParkNo(String carParkNo) { this.carParkNo = carParkNo; }
    public Integer getTotalLots() { return totalLots; }
    public void setTotalLots(Integer totalLots) { this.totalLots = totalLots; }
    public Integer getLotsAvailable() { return lotsAvailable; }
    public void setLotsAvailable(Integer lotsAvailable) { this.lotsAvailable = lotsAvailable; }
    public String getLotType() { return lotType; }
    public void setLotType(String lotType) { this.lotType = lotType; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public Boolean getIsStale() { return isStale; }
    public void setIsStale(Boolean isStale) { this.isStale = isStale; }
}
