package com.prakritikishore.carparkservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GovernmentApiResponse(
        @JsonProperty("items") List<Item> items
) {
    public record Item(
            @JsonProperty("timestamp") String timestamp,
            @JsonProperty("carpark_data") List<CarParkData> carParkData
    ) {}

    public record CarParkData(
            @JsonProperty("carpark_number") String carParkNumber,
            @JsonProperty("update_datetime") String updateDatetime,
            @JsonProperty("carpark_info") List<CarParkInfo> carParkInfo
    ) {}

    public record CarParkInfo(
            @JsonProperty("total_lots") String totalLots,
            @JsonProperty("lot_type") String lotType,
            @JsonProperty("lots_available") String lotsAvailable
    ) {}
}
