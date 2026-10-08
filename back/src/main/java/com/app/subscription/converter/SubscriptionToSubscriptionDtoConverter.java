package com.app.subscription.converter;

import com.app.subscription.Subscription;
import com.app.subscription.SubscriptionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionToSubscriptionDtoConverter implements Converter<Subscription, SubscriptionDto> {
    @Override
    public SubscriptionDto convert(Subscription source) {
        return new SubscriptionDto(
                source.getId(),

                source.getName(),
                source.getDuration(),
                source.getPrice()
        );
    }
}
