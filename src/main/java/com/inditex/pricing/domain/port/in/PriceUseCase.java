package com.inditex.pricing.domain.port.in;

import com.inditex.pricing.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceUseCase {
    Optional<Price> getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
