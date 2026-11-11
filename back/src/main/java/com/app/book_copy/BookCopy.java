package com.app.book_copy;

import com.app.book.Book;
import com.app.book_ordering.BookOrdering;
import com.app.enums.BookCopyStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BookCopy implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_copy_g")
    @SequenceGenerator(name = "book_copy_g", sequenceName = "book_copy_seq", allocationSize = 1)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String inventoryNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private BookCopyStatus status = BookCopyStatus.AVAILABLE;

    @Column(nullable = false)
    private LocalDate dateAdded = LocalDate.now();

    @ManyToOne(optional = false)
    private Book book;
    @OneToMany(mappedBy = "bookCopy")
    private List<BookOrdering> orderings = new ArrayList<>();

    public BookCopy(String inventoryNumber, Book book) {
        this.inventoryNumber = inventoryNumber;
        this.book = book;
    }

    public boolean isActive() {
        return status != BookCopyStatus.WRITTEN_OFF;
    }

}
