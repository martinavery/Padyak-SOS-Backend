package com.padyakrescue.shop;

import com.padyakrescue.shop.dto.AddShopRequest;
import com.padyakrescue.shop.dto.ShopDto;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopDto addShop(@Valid @RequestBody AddShopRequest request) {
        return shopService.addShop(request);
    }

    @GetMapping("/nearby")
    public List<ShopDto> getNearbyShops(@RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusMeters,
            @RequestParam(defaultValue = "10") int limit) {
        return shopService.getNearbyShops(lat, lng, radiusMeters, limit);
    }

    @GetMapping("/sync")
    public List<ShopDto> syncShops(@RequestParam OffsetDateTime since) {
        return shopService.syncShops(since);
    }
}
