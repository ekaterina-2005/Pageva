package com.app.subscription_ordering;

import com.app.appUser.AppUser;
import com.app.subscription.Subscription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

import static com.app.util.Global.getDateNow;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubscriptionOrdering implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "subscription_g")
    @SequenceGenerator(name = "subscription_g", sequenceName = "subscription_seq", allocationSize = 1)
    private Long id;

    private String card;
    private String cvv;

    private String name;
    private int duration;
    private float price;

    private String date = getDateNow();

    @ManyToOne
    private AppUser owner;

    public SubscriptionOrdering(String card, String cvv) {
        this.card = card;
        this.cvv = cvv;
    }

    public void setSubscription(Subscription subscription) {
        this.name = subscription.getName();
        this.duration = subscription.getDuration();
        this.price = subscription.getPrice();
    }

    public boolean isActive() {
        try {
            LocalDate startDate = LocalDate.parse(date);
            LocalDate endDate = startDate.plusDays(duration);
            return !LocalDate.now().isAfter(endDate);
        } catch (Exception e) {
            return false;
        }
    }

}