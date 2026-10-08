package com.app.subscription_ordering.converter;

import com.app.subscription_ordering.SubscriptionOrdering;
import com.app.subscription_ordering.SubscriptionOrderingDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionOrderingToSubscriptionOrderingDtoConverter implements Converter<SubscriptionOrdering, SubscriptionOrderingDto> {
    @Override
    public SubscriptionOrderingDto convert(SubscriptionOrdering source) {
        return new SubscriptionOrderingDto(
                source.getId(),

                source.getCard(),
                source.getCvv(),

                source.getName(),
                source.getDuration(),
                source.getPrice(),

                source.getDate(),
                source.isActive()

        );
    }
}
