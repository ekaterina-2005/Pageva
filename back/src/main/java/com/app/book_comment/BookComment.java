package com.app.book_comment;

import com.app.appUser.AppUser;
import com.app.book.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static com.app.util.Global.getDateAndTimeNow;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BookComment implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_comment_g")
    @SequenceGenerator(name = "book_comment_g", sequenceName = "book_comment_seq", allocationSize = 1)
    private Long id;

    private String date = getDateAndTimeNow();

    private int score;

    @Column(length = 5000)
    private String text;

    @ManyToOne
    private Book book;
    @ManyToOne
    private AppUser owner;

    public BookComment(int score, String text) {
        this.score = score;
        this.text = text;
    }

    public void update(BookComment update) {
        this.score = update.getScore();
        this.text = update.getText();
    }
}