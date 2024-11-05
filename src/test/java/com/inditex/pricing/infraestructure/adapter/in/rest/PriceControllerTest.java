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

}
