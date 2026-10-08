package com.app.subscription;

import com.app.subscription_ordering.SubscriptionOrdering;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Subscription implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "subscription_g")
    @SequenceGenerator(name = "subscription_g", sequenceName = "subscription_seq", allocationSize = 1)
    private Long id;

    private String name;
    private int duration;
    private float price;

    public Subscription(String name, int duration, float price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public void update(Subscription update) {
        this.name = update.getName();
        this.duration = update.getDuration();
        this.price = update.getPrice();
    }

}