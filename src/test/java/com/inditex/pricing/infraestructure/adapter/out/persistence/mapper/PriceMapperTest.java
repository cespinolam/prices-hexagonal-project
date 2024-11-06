package com.inditex.pricing.infraestructure.adapter.out.persistence.mapper;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.infrastructure.adapter.in.rest.PriceResponse;
import com.inditex.pricing.infrastructure.adapter.out.persistence.mapper.PriceMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceMapperTest {

    private final PriceMapper priceMapper = new PriceMapper();

    @Test
    void testToResponse() {
        Price price = new Price(1L,1L,1L,1,
                new BigDecimal("50.00"),"EUR", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1));
        PriceResponse response = priceMapper.toResponse(price);

        assertEquals(price.getProductId(), response.getProductId());
        assertEquals(price.getPrice(), response.getPrice());
        assertEquals(price.getCurrency(), response.getCurrency());
    }
}

