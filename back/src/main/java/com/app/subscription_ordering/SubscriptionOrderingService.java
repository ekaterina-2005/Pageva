package com.app.subscription_ordering;

import com.app.appUser.UserService;
import com.app.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionOrderingService {

    private final SubscriptionOrderingRepository repository;
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public List<SubscriptionOrdering> findAll() {
        return userService.getCurrentUser().getSubscriptionOrderings();
    }

    public boolean isActive() {
        List<SubscriptionOrdering> orderings = userService.getCurrentUser().getSubscriptionOrderings();

        for (SubscriptionOrdering ordering : orderings) {
            if (ordering.isActive()) {
                return true;
            }
        }

        return false;
    }

    public SubscriptionOrdering save(SubscriptionOrdering save, String subscriptionId) {
        save.setSubscription(subscriptionService.find(subscriptionId));
        save.setOwner(userService.getCurrentUser());
        return repository.save(save);
    }

}
