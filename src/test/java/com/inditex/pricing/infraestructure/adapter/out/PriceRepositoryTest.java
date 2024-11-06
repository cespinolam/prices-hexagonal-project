package com.inditex.pricing.infraestructure.adapter.out;

import com.inditex.pricing.infrastructure.adapter.out.persistence.PriceEntity;
import com.inditex.pricing.infrastructure.adapter.out.persistence.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void testFindApplicablePrices() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");
        List<PriceEntity> prices = priceRepository.findApplicablePrices(1L, 35455L, dateTime);

        assertFalse(prices.isEmpty(), "Should return applicable prices");
    }
}
