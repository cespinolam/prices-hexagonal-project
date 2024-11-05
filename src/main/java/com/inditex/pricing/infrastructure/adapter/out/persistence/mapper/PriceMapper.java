package com.inditex.pricing.infrastructure.adapter.out.persistence.mapper;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.infrastructure.adapter.in.rest.PriceResponse;
import com.inditex.pricing.infrastructure.adapter.out.persistence.PriceEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    public Price toDomain(PriceEntity priceEntity) {
        if (priceEntity == null) {
            return null;
        }
        return new Price(
                priceEntity.getBrandId(),
                priceEntity.getPriceList(),
                priceEntity.getProductId(),
                priceEntity.getPriority(),
                priceEntity.getPrice(),
                priceEntity.getCurrency(),
                priceEntity.getStartDate(),
                priceEntity.getEndDate()
        );
    }

    public PriceEntity toEntity(Price price) {
        if (price == null) {
            return null;
        }
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(price.getBrandId());
        priceEntity.setStartDate(price.getStartDate());
        priceEntity.setEndDate(price.getEndDate());
        priceEntity.setPriceList(price.getPriceList());
        priceEntity.setProductId(price.getProductId());
        priceEntity.setPriority(price.getPriority());
        priceEntity.setPrice(price.getPrice());
        priceEntity.setCurrency(price.getCurrency());
        return priceEntity;
    }

    public PriceResponse toResponse(Price price) {
        if (price == null) {
            return null;
        }
        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurrency()
        );
    }
}

