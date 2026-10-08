package com.app.discount.converter;

import com.app.discount.Discount;
import com.app.discount.DiscountDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DiscountDtoToDiscountConverter implements Converter<DiscountDto, Discount> {
    @Override
    public Discount convert(DiscountDto source) {
        return new Discount(
                source.name(),
                source.percent()
        );
    }
}
