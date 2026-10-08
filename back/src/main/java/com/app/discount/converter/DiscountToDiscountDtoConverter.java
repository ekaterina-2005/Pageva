package com.app.discount.converter;

import com.app.discount.Discount;
import com.app.discount.DiscountDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DiscountToDiscountDtoConverter implements Converter<Discount, DiscountDto> {
    @Override
    public DiscountDto convert(Discount source) {
        return new DiscountDto(
                source.getId(),

                source.getName(),
                source.getPercent()
        );
    }
}
