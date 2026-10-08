package com.app.book;

import com.app.book_comment.BookComment;
import com.app.book_ordering.BookOrdering;
import com.app.category.Category;
import com.app.enums.BookAge;
import com.app.enums.BookOrderingStatus;
import com.app.enums.BookType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.app.util.Global.round;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Book implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_g")
    @SequenceGenerator(name = "book_g", sequenceName = "book_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String author;
    private int page;
    private String date;
    private String read;
    private float price;
    private int quantity;

    private boolean repair = false;
    private boolean visibility = true;

    @Column(length = 5000)
    private String description;
    @Column(length = 1000)
    private String img = "";
    @Column(length = 1000)
    private String file = "";

    @Enumerated(EnumType.STRING)
    private BookAge age;
    @Enumerated(EnumType.STRING)
    private BookType type;

    @ManyToOne
    private Category category;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookComment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookOrdering> orderings = new ArrayList<>();

    public Book(Long id, String name, String author, int page, String date, String read, float price, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.page = page;
        this.date = date;
        this.read = read;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public Book(String name, String author, int page, String date, String read, float price, int quantity, String description) {
        this.name = name;
        this.author = author;
        this.page = page;
        this.date = date;
        this.read = read;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public void update(Book update) {
        this.name = update.getName();
        this.author = update.getAuthor();
        this.page = update.getPage();
        this.date = update.getDate();
        this.read = update.getRead();
        this.price = update.getPrice();
        this.quantity = update.getQuantity();
        this.description = update.getDescription();
    }

    public int getOrderingsSize() {
        return orderings.size();
    }

    public int getQuantityFree() {
        return quantity - orderings.stream().reduce(0, (i, ordering) -> {
            if (ordering.getStatus() != BookOrderingStatus.RETURNED &&
                    ordering.getStatus() != BookOrderingStatus.REJECTED
            ) return i + 1;
            return i;
        }, Integer::sum);
    }

    public int getCount() {
        return orderings.stream().reduce(0, (i, ordering) -> {
            if (ordering.getStatus() == BookOrderingStatus.RETURNED) return i + 1;
            return i;
        }, Integer::sum);
    }

    public List<BookComment> getComments() {
        comments.sort(Comparator.comparing(BookComment::getId));
        Collections.reverse(comments);
        return comments;
    }

    public float getScore() {
        return round((float) comments.stream().reduce(0, (i, comment) -> i + comment.getScore(), Integer::sum) / comments.size());
    }

    public int getReadMinute() {
        if (getScore() == 0) return 0;
        String[] parts = read.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

}