package com.app.appUser;

import com.app.book_comment.BookComment;
import com.app.book_ordering.BookOrdering;
import com.app.enums.Role;
import com.app.subscription_ordering.SubscriptionOrdering;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.app.util.Global.getDateNow;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "app_user_g")
    @SequenceGenerator(name = "app_user_g", sequenceName = "app_user_seq", allocationSize = 1)
    private Long id;

    @Size(min = 1, max = 255, message = "username is required length 1-255")
    @NotEmpty(message = "username is required")
    private String username;
    @Size(min = 1, max = 255, message = "password is required length 1-255")
    @NotEmpty(message = "password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Size(min = 1, max = 255, message = "fio is required length 1-255")
    @NotEmpty(message = "fio is required")
    private String fio = "ФИО";
    @Size(min = 1, max = 255, message = "email is required length 1-255")
    @NotEmpty(message = "email is required")
    @Email(message = "email is required")
    private String email = "Почта";

    private String date = getDateNow();

    @Column(length = 1000)
    private String img = "/img/avatar.png";

    private boolean accountNonLocked = true;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<BookComment> bookComments = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<BookOrdering> bookOrderings = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<SubscriptionOrdering> subscriptionOrderings = new ArrayList<>();

    public AppUser(String username) {
        this.username = username;
    }

    public AppUser(String fio, String date, String email) {
        this.fio = fio;
        this.date = date;
        this.email = email;
    }

    public void update(AppUser update) {
        this.fio = update.getFio();
        this.date = update.getDate();
        this.email = update.getEmail();
    }

}