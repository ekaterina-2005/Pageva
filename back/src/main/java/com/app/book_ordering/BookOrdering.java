package com.app.book_ordering;

import com.app.appUser.AppUser;
import com.app.book.Book;
import com.app.book_copy.BookCopy;
import com.app.discount.Discount;
import com.app.enums.BookOrderingStatus;
import com.app.fine.Fine;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static com.app.util.Global.getDateNow;
import static com.app.util.Global.round;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BookOrdering implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_ordering_g")
    @SequenceGenerator(name = "book_ordering_g", sequenceName = "book_ordering_seq", allocationSize = 1)
    private Long id;

    private String date;
    private int days;
    @Column(name = "ordering_percent")
    private int percent = 0;
    private float price;

    private String dateStart = getDateNow();
    private String dateEnd = "";

    @Enumerated(EnumType.STRING)
    private BookOrderingStatus status = BookOrderingStatus.WAITING;

    @ManyToOne
    private Book book;
    @ManyToOne
    private BookCopy bookCopy;
    @ManyToOne
    private Discount discount;
    @ManyToOne
    private AppUser owner;

    @OneToOne(mappedBy = "ordering", cascade = CascadeType.ALL)
    private Fine fine;

    public BookOrdering(String date, int days) {
        this.date = date;
        this.days = days;
    }

    public void setBook(Book book) {
        this.price = book.getPrice();
        this.book = book;
    }

    public void setDiscount(Discount discount) {
        if (discount != null) this.percent = discount.getPercent();
        else this.percent = 0;
        this.discount = discount;
    }

    public float getSum() {
        if (price == 0) return 0;
        return round((days * price) * ((float) (100 - percent) / 100));
    }

    public Long getDiscountId() {
        return discount == null ? 0L : discount.getId();
    }

    public String getDiscountName() {
        return discount == null ? "" : discount.getName();
    }

    public Long getBookCopyId() {
        return bookCopy == null ? 0L : bookCopy.getId();
    }

    public String getInventoryNumber() {
        return bookCopy == null ? "" : bookCopy.getInventoryNumber();
    }

    // дни просрочки: дата возврата (или сегодня, если книга у читателя) минус (дата начала аренды + дни аренды)
    public int getOverdueDays() {
        if (status == BookOrderingStatus.DONE) return getOverdueDays(LocalDate.now());
        if (status == BookOrderingStatus.RETURNED && dateEnd != null && !dateEnd.isEmpty()) {
            try {
                return getOverdueDays(LocalDate.parse(dateEnd));
            } catch (DateTimeParseException e) {
                return 0;
            }
        }
        return 0;
    }

    public int getOverdueDays(LocalDate returnDate) {
        try {
            LocalDate dueDate = LocalDate.parse(date).plusDays(days);
            return (int) Math.max(0, ChronoUnit.DAYS.between(dueDate, returnDate));
        } catch (DateTimeParseException | NullPointerException e) {
            return 0;
        }
    }
}