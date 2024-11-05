package com.inditex.pricing.application.service;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.domain.port.in.PriceUseCase;
import com.inditex.pricing.domain.port.out.PriceRepositoryPort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceApplicationService implements PriceUseCase {
    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Optional<Price> getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceRepositoryPort.findApplicablePrice(brandId, productId, applicationDate);
    }
}
