package com.padyakrescue.shop.dto;

import com.padyakrescue.shop.ShopType;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ShopDto {

    private UUID id;
    private String name;
    private ShopType type;
    private double latitude;
    private double longitude;
    private OffsetDateTime lastUpdated;

    public ShopDto(UUID id, String name, ShopType type, double latitude, double longitude, OffsetDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdated = lastUpdated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopType getType() {
        return type;
    }

    public void setType(ShopType type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
