package com.prakritikishore.carparkservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carpark")
public class CarPark {

    @Id
    @Column(name = "car_park_no", nullable = false, length = 20)
    private String carParkNo;

    @Column(name = "address")
    private String address;

    @Column(name = "x_coord")
    private Double xCoord;

    @Column(name = "y_coord")
    private Double yCoord;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "car_park_type")
    private String carParkType;

    public CarPark() {}

    public CarPark(String carParkNo, String address, Double xCoord, Double yCoord, Double latitude, Double longitude, String carParkType) {
        this.carParkNo = carParkNo;
        this.address = address;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.latitude = latitude;
        this.longitude = longitude;
        this.carParkType = carParkType;
    }

    public String getCarParkNo() { return carParkNo; }
    public void setCarParkNo(String carParkNo) { this.carParkNo = carParkNo; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getxCoord() { return xCoord; }
    public void setxCoord(Double xCoord) { this.xCoord = xCoord; }
    public Double getyCoord() { return yCoord; }
    public void setyCoord(Double yCoord) { this.yCoord = yCoord; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getCarParkType() { return carParkType; }
    public void setCarParkType(String carParkType) { this.carParkType = carParkType; }
}
