package com.padyakrescue.geo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeoFactory {

    private final GeometryFactory geometryFactory;

    public GeoFactory() {
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public Point createPoint(double latitude, double longitude) {
        // JTS uses (x, y) which corresponds to (longitude, latitude)
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
