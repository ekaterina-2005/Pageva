package com.app.discount;

import com.app.book_ordering.BookOrdering;
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
public class Discount implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "discount_g")
    @SequenceGenerator(name = "discount_g", sequenceName = "discount_seq", allocationSize = 1)
    private Long id;

    private String name;
    private int percent;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<BookOrdering> orderings = new ArrayList<>();

    public Discount(Long id, String name, int percent) {
        this.id = id;
        this.name = name;
        this.percent = percent;
    }

    public Discount(String name, int percent) {
        this.name = name;
        this.percent = percent;
    }

    public void update(Discount update) {
        this.name = update.getName();
        this.percent = update.getPercent();
    }
}