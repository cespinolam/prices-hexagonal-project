package com.inditex.pricing.e2e;

import com.inditex.pricing.infrastructure.adapter.out.persistence.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PriceApplicationE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void testGetPrice_At10AM_14thJune() throws Exception {
        performRequestAndVerify(1L, 35455L, LocalDateTime.parse("2020-06-14T10:00:00"), BigDecimal.valueOf(35.5));
    }

    @Test
    void testGetPrice_At4PM_14thJune() throws Exception {
        performRequestAndVerify(1L, 35455L, LocalDateTime.parse("2020-06-14T16:00:00"), BigDecimal.valueOf(25.45));
    }

    @Test
    void testGetPrice_At9PM_14thJune() throws Exception {
        performRequestAndVerify(1L, 35455L, LocalDateTime.parse("2020-06-14T21:00:00"), BigDecimal.valueOf(35.5));
    }

    @Test
    void testGetPrice_At10AM_15thJune() throws Exception {
        performRequestAndVerify(1L, 35455L, LocalDateTime.parse("2020-06-15T10:00:00"), BigDecimal.valueOf(30.5));
    }

    @Test
    void testGetPrice_At9PM_16thJune() throws Exception {
        performRequestAndVerify(1L, 35455L, LocalDateTime.parse("2020-06-16T21:00:00"), BigDecimal.valueOf(38.95));
    }

    private void performRequestAndVerify(Long brandId, Long productId, LocalDateTime applicationDate, BigDecimal expectedPrice) throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("brandId", brandId.toString())
                        .param("productId", productId.toString())
                        .param("applicationDate", applicationDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(expectedPrice.doubleValue()));
    }
}
