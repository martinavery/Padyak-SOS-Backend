package com.padyakrescue.shop;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShopType type;

    @Column(nullable = false, columnDefinition = "geography(Point, 4326)")
    private Point location;

    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    public Shop() {
    }

    public Shop(String name, ShopType type, Point location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = OffsetDateTime.now();
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
