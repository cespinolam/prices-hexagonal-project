package com.inditex.pricing.infrastructure.adapter.in.rest;

import com.inditex.pricing.domain.port.in.PriceUseCase;
import com.inditex.pricing.infrastructure.adapter.out.persistence.mapper.PriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prices")
public class PriceController {
    private final PriceUseCase priceUseCase;
    private final PriceMapper priceMapper;

    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam Long brandId,
            @RequestParam Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        return priceUseCase.getApplicablePrice(brandId, productId, applicationDate)
                .map(price -> new ResponseEntity<>(priceMapper.toResponse(price), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
