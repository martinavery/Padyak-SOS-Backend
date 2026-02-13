package com.padyakrescue.shop;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padyakrescue.shop.dto.ShopDto;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ShopController.class)
class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShopService shopService;

    // Test GET /shops/nearby with all required parameters - happy path
    @Test
    void getNearbyShops_withAllParams_returns200AndList() throws Exception {
        // Arrange: Create a mock ShopDto response
        UUID shopId = UUID.randomUUID();
        ShopDto mockShop = new ShopDto(
                shopId,
                "Bike Repair Shop",
                ShopType.BIKESHOP,
                14.5995,
                120.9842,
                OffsetDateTime.now()
        );

        when(shopService.getNearbyShops(14.5995, 120.9842, 1000.0, 10))
                .thenReturn(List.of(mockShop));

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/shops/nearby")
                        .param("lat", "14.5995")
                        .param("lng", "120.9842")
                        .param("radiusMeters", "1000")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(shopId.toString()))
                .andExpect(jsonPath("$[0].name").value("Bike Repair Shop"))
                .andExpect(jsonPath("$[0].type").value("BIKESHOP"));
    }

    // Test GET /shops/nearby with default limit parameter
    @Test
    void getNearbyShops_withoutLimit_usesDefaultLimit10() throws Exception {
        // Arrange: Mock service to return empty list
        when(shopService.getNearbyShops(anyDouble(), anyDouble(), anyDouble(), eq(10)))
                .thenReturn(List.of());

        // Act & Assert: Perform GET request without limit param
        mockMvc.perform(get("/shops/nearby")
                        .param("lat", "14.5995")
                        .param("lng", "120.9842")
                        .param("radiusMeters", "1000"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Verify service was called with default limit of 10
        verify(shopService).getNearbyShops(14.5995, 120.9842, 1000.0, 10);
    }

    // Test GET /shops/nearby with missing required parameters
    @Test
    void getNearbyShops_missingRequiredParams_returns400() throws Exception {
        // Act & Assert: Perform GET request without radiusMeters param
        mockMvc.perform(get("/shops/nearby")
                        .param("lat", "14.5995")
                        .param("lng", "120.9842"))
                .andExpect(status().isBadRequest());
    }

    // Test POST /shops with valid request body
    @Test
    void addShop_withValidRequest_returns201() throws Exception {
        // Arrange: Create valid request JSON
        String requestJson = """
                {
                  "name": "Vulcanizing Shop",
                  "type": "VULCANIZING",
                  "latitude": 14.5995,
                  "longitude": 120.9842
                }
                """;

        // Mock service response
        UUID shopId = UUID.randomUUID();
        ShopDto mockResponse = new ShopDto(
                shopId,
                "Vulcanizing Shop",
                ShopType.VULCANIZING,
                14.5995,
                120.9842,
                OffsetDateTime.now()
        );

        when(shopService.addShop(any())).thenReturn(mockResponse);

        // Act & Assert: Perform POST request and verify response
        mockMvc.perform(post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(shopId.toString()))
                .andExpect(jsonPath("$.name").value("Vulcanizing Shop"))
                .andExpect(jsonPath("$.type").value("VULCANIZING"));
    }

    // Test POST /shops with validation error - empty body
    @Test
    void addShop_withEmptyBody_returns400() throws Exception {
        // Act & Assert: Perform POST request with empty JSON body
        mockMvc.perform(post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // Test POST /shops with validation error - latitude out of range
    @Test
    void addShop_withInvalidLatitude_returns400() throws Exception {
        // Arrange: Create request with latitude > 90 (invalid)
        String requestJson = """
                {
                  "name": "Test Shop",
                  "type": "BIKESHOP",
                  "latitude": 91.0,
                  "longitude": 120.9842
                }
                """;

        // Act & Assert: Perform POST request and verify validation error
        mockMvc.perform(post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    // Test GET /shops/sync with valid since parameter
    @Test
    void syncShops_withValidSince_returns200() throws Exception {
        // Arrange: Create mock response
        UUID shopId = UUID.randomUUID();
        ShopDto mockShop = new ShopDto(
                shopId,
                "Water Station",
                ShopType.WATER,
                14.5995,
                120.9842,
                OffsetDateTime.now()
        );

        OffsetDateTime since = OffsetDateTime.parse("2026-02-13T00:00:00+00:00");
        when(shopService.syncShops(any(OffsetDateTime.class)))
                .thenReturn(List.of(mockShop));

        // Act & Assert: Perform GET request with since parameter
        mockMvc.perform(get("/shops/sync")
                        .param("since", "2026-02-13T00:00:00+00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(shopId.toString()))
                .andExpect(jsonPath("$[0].name").value("Water Station"))
                .andExpect(jsonPath("$[0].type").value("WATER"));
    }
}
