package com.app.subscription_ordering;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionOrderingRepository extends JpaRepository<SubscriptionOrdering, Long> {
}
