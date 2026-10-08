package com.app.subscription.converter;

import com.app.subscription.Subscription;
import com.app.subscription.SubscriptionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionDtoToSubscriptionConverter implements Converter<SubscriptionDto, Subscription> {
    @Override
    public Subscription convert(SubscriptionDto source) {
        return new Subscription(
                source.name(),
                source.duration(),
                source.price()
        );
    }
}
