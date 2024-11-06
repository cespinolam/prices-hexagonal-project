package com.inditex.pricing.infraestructure.adapter.in.rest;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.port.in.PriceUseCase;
import com.inditex.pricing.infrastructure.adapter.in.rest.PriceController;
import com.inditex.pricing.infrastructure.adapter.in.rest.PriceResponse;
import com.inditex.pricing.infrastructure.adapter.out.persistence.mapper.PriceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceUseCase priceUseCase;

    @MockBean
    private PriceMapper priceMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private void setupMockResponse(LocalDateTime applicationDate, Long priceList, BigDecimal priceValue) {
        Long brandId = 1L;
        Long productId = 35455L;
        Price mockPrice = Price.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(priceList)
                .startDate(applicationDate.minusHours(2))
                .endDate(applicationDate.plusHours(2))
                .price(priceValue)
                .currency("EUR")
                .build();

        PriceResponse mockResponse = PriceResponse.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(priceList)
                .startDate(mockPrice.getStartDate())
                .endDate(mockPrice.getEndDate())
                .price(priceValue)
                .currency("EUR")
                .build();

        when(priceUseCase.getApplicablePrice(brandId, productId, applicationDate)).thenReturn(Optional.of(mockPrice));
        when(priceMapper.toResponse(mockPrice)).thenReturn(mockResponse);
    }

    @Test
     void testGetPriceFound() throws Exception {
        Long brandId = 1L;
        Long productId = 35455L;
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");

        Price mockPrice = Price.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .price(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .build();

        PriceResponse mockResponse = PriceResponse.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .price(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .build();

        when(priceUseCase.getApplicablePrice(brandId, productId, applicationDate)).thenReturn(Optional.of(mockPrice));
        when(priceMapper.toResponse(mockPrice)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/prices")
                        .param("brandId", String.valueOf(brandId))
                        .param("productId", String.valueOf(productId))
                        .param("applicationDate", applicationDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.priceList").value(1L))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
     void testGetPriceNotFound() throws Exception {
        Long brandId = 1L;
        Long productId = 35455L;
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");

        when(priceUseCase.getApplicablePrice(brandId, productId, applicationDate)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/prices")
                        .param("brandId", String.valueOf(brandId))
                        .param("productId", String.valueOf(productId))
                        .param("applicationDate", applicationDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPrice_withInvalidBrandId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("brandId", "invalid")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPriceAt10AMOn14th() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00", formatter);
        setupMockResponse(applicationDate, 1L, new BigDecimal("35.50"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value("35.50"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testGetPriceAt4PMOn14th() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T16:00:00", formatter);
        setupMockResponse(applicationDate, 2L, new BigDecimal("25.45"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value("25.45"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testGetPriceAt9PMOn14th() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T21:00:00", formatter);
        setupMockResponse(applicationDate, 3L, new BigDecimal("30.50"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value("30.50"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testGetPriceAt10AMOn15th() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-15T10:00:00", formatter);
        setupMockResponse(applicationDate, 4L, new BigDecimal("38.95"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value("38.95"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testGetPriceAt9PMOn16th() throws Exception {
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-16T21:00:00", formatter);
        setupMockResponse(applicationDate, 5L, new BigDecimal("40.25"));

        mockMvc.perform(get("/api/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priceList").value(5))
                .andExpect(jsonPath("$.price").value("40.25"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }
}
