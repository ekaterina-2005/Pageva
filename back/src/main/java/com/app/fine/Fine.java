package com.app.fine;

import com.app.book_ordering.BookOrdering;
import com.app.enums.FineStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Fine implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "fine_g")
    @SequenceGenerator(name = "fine_g", sequenceName = "fine_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private int daysOverdue;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private FineStatus status = FineStatus.UNPAID;

    @Column(nullable = false)
    private LocalDate dateCreated = LocalDate.now();
    private LocalDate datePaid;

    @OneToOne(optional = false)
    @JoinColumn(name = "ordering_id", nullable = false, unique = true)
    private BookOrdering ordering;

    public Fine(BookOrdering ordering, int daysOverdue, BigDecimal amount) {
        this.ordering = ordering;
        this.daysOverdue = daysOverdue;
        this.amount = amount;
    }

}
