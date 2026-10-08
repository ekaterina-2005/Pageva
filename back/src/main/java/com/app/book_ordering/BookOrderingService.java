package com.app.book_ordering;

import com.app.appUser.AppUser;
import com.app.appUser.UserService;
import com.app.book.BookService;
import com.app.discount.DiscountService;
import com.app.enums.BookOrderingStatus;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.app.util.Global.getDateNow;

@Service
@RequiredArgsConstructor
public class BookOrderingService {

    private final BookOrderingRepository repository;
    private final BookService bookService;
    private final DiscountService discountService;
    private final UserService userService;

    public List<BookOrdering> findAll() {
        List<BookOrdering> orderings = new ArrayList<>();

        AppUser user = userService.getCurrentUser();

        switch (user.getRole()) {
            case USER -> orderings = user.getBookOrderings();
            case ADMIN, MANAGER -> orderings = repository.findAll();
        }

        orderings.sort(Comparator.comparing(BookOrdering::getId));
        Collections.reverse(orderings);

        return orderings;
    }

    public List<BookOrdering> findAllByStatus(BookOrderingStatus status) {
        return repository.findAllByStatus(status);
    }

    public BookOrdering find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена заявка по ИД: " + id));
    }

    public BookOrdering booked(String id) {
        BookOrdering ordering = find(id);
        ordering.setStatus(BookOrderingStatus.BOOKED);
        return repository.save(ordering);
    }

    public BookOrdering rejected(String id) {
        BookOrdering ordering = find(id);
        ordering.setStatus(BookOrderingStatus.REJECTED);
        return repository.save(ordering);
    }

    public BookOrdering done(String id) {
        BookOrdering ordering = find(id);
        ordering.setStatus(BookOrderingStatus.DONE);
        return repository.save(ordering);
    }

    public BookOrdering returned(String id) {
        BookOrdering ordering = find(id);
        ordering.setStatus(BookOrderingStatus.RETURNED);
        ordering.setDateEnd(getDateNow());
        return repository.save(ordering);
    }

    public BookOrdering save(BookOrdering save, String bookId, String discountId) {
        save.setOwner(userService.getCurrentUser());
        save.setBook(bookService.find(bookId));
        if (!discountId.equals("0")) save.setDiscount(discountService.find(discountId));
        return repository.save(save);
    }

    public BookOrdering updateDiscount(String id, String discountId) {
        BookOrdering ordering = find(id);
        if (!discountId.equals("0")) ordering.setDiscount(discountService.find(discountId));
        else ordering.setDiscount(null);
        return repository.save(ordering);
    }

}
