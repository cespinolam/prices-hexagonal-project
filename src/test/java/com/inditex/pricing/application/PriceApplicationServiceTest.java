package com.inditex.pricing.application;

import com.inditex.pricing.application.service.PriceApplicationService;
import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PriceApplicationServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private PriceApplicationService priceApplicationService;

    public PriceApplicationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetApplicablePrice() {
        Long brandId = 1L;
        Long productId = 35455L;
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        Price expectedPrice = Price.builder()
                .brandId(brandId)
                .priceList(1L)
                .productId(productId)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .build();

        when(priceRepositoryPort.findApplicablePrice(brandId, productId, applicationDate)).thenReturn(Optional.of(expectedPrice));

        Optional<Price> actualPrice = priceApplicationService.getApplicablePrice(brandId, productId, applicationDate);

        assertEquals(expectedPrice, actualPrice.get());
    }
}
