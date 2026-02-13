package com.padyakrescue.shop;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {

    @Query(value = "SELECT * FROM shops s " +
            "WHERE ST_DWithin(s.location, :point, :radius, false) " +
            "ORDER BY ST_Distance(s.location, :point) ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Shop> findNearby(@Param("point") Point point,
            @Param("radius") double radius,
            @Param("limit") int limit);

    List<Shop> findAllByLastUpdatedAfter(OffsetDateTime since);
}
