package com.padyakrescue.shop;

import com.padyakrescue.geo.GeoFactory;
import com.padyakrescue.shop.dto.AddShopRequest;
import com.padyakrescue.shop.dto.ShopDto;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;
    private final GeoFactory geoFactory;

    public ShopService(ShopRepository shopRepository, GeoFactory geoFactory) {
        this.shopRepository = shopRepository;
        this.geoFactory = geoFactory;
    }

    @Transactional
    public ShopDto addShop(AddShopRequest request) {
        Point location = geoFactory.createPoint(request.getLatitude(), request.getLongitude());
        Shop shop = new Shop(request.getName(), request.getType(), location);
        Shop savedShop = shopRepository.save(shop);
        return mapToDto(savedShop);
    }

    public List<ShopDto> getNearbyShops(double lat, double lng, double radiusMeters, int limit) {
        Point point = geoFactory.createPoint(lat, lng);
        List<Shop> shops = shopRepository.findNearby(point, radiusMeters, limit);
        return shops.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ShopDto> syncShops(OffsetDateTime since) {
        List<Shop> shops = shopRepository.findAllByLastUpdatedAfter(since);
        return shops.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ShopDto mapToDto(Shop shop) {
        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.getType(),
                shop.getLocation().getY(), // Latitude
                shop.getLocation().getX(), // Longitude
                shop.getLastUpdated());
    }
}
