package com.app.subscription_ordering.converter;

import com.app.subscription.converter.SubscriptionToSubscriptionDtoConverter;
import com.app.subscription_ordering.SubscriptionOrdering;
import com.app.subscription_ordering.SubscriptionOrderingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionOrderingDtoToSubscriptionOrderingConverter implements Converter<SubscriptionOrderingDto, SubscriptionOrdering> {
    @Override
    public SubscriptionOrdering convert(SubscriptionOrderingDto source) {
        return new SubscriptionOrdering(
                source.card(),
                source.cvv()
        );
    }
}
