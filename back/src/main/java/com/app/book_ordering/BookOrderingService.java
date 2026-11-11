package com.app.book_ordering;

import com.app.appUser.AppUser;
import com.app.appUser.UserService;
import com.app.book.Book;
import com.app.book.BookService;
import com.app.book_copy.BookCopy;
import com.app.book_copy.BookCopyService;
import com.app.discount.DiscountService;
import com.app.enums.BookCopyStatus;
import com.app.enums.BookOrderingStatus;
import com.app.enums.BookType;
import com.app.fine.FineService;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BookCopyService bookCopyService;
    private final FineService fineService;

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
        return booked(id, "0");
    }

    // бронирование закрепляет за заявкой конкретный экземпляр (copyId = "0" — первый свободный)
    @Transactional
    public BookOrdering booked(String id, String copyId) {
        BookOrdering ordering = find(id);
        checkStatus(ordering, BookOrderingStatus.WAITING);
        if (isOrdinary(ordering)) {
            BookCopy copy = bookCopyService.findAvailable(ordering.getBook(), copyId);
            ordering.setBookCopy(bookCopyService.setStatus(copy, BookCopyStatus.RESERVED));
        }
        ordering.setStatus(BookOrderingStatus.BOOKED);
        return repository.save(ordering);
    }

    @Transactional
    public BookOrdering rejected(String id) {
        BookOrdering ordering = find(id);
        checkStatus(ordering, BookOrderingStatus.WAITING, BookOrderingStatus.BOOKED);
        if (ordering.getBookCopy() != null) {
            bookCopyService.setStatus(ordering.getBookCopy(), BookCopyStatus.AVAILABLE);
            ordering.setBookCopy(null);
        }
        ordering.setStatus(BookOrderingStatus.REJECTED);
        return repository.save(ordering);
    }

    @Transactional
    public BookOrdering done(String id) {
        BookOrdering ordering = find(id);
        checkStatus(ordering, BookOrderingStatus.BOOKED);
        if (ordering.getBookCopy() != null) bookCopyService.setStatus(ordering.getBookCopy(), BookCopyStatus.ISSUED);
        ordering.setStatus(BookOrderingStatus.DONE);
        return repository.save(ordering);
    }

    // возврат освобождает экземпляр и при просрочке начисляет штраф
    @Transactional
    public BookOrdering returned(String id) {
        BookOrdering ordering = find(id);
        checkStatus(ordering, BookOrderingStatus.DONE);
        if (ordering.getBookCopy() != null) bookCopyService.setStatus(ordering.getBookCopy(), BookCopyStatus.AVAILABLE);
        ordering.setStatus(BookOrderingStatus.RETURNED);
        ordering.setDateEnd(getDateNow());
        BookOrdering saved = repository.save(ordering);
        fineService.charge(saved);
        return saved;
    }

    public BookOrdering save(BookOrdering save, String bookId, String discountId) {
        AppUser user = userService.getCurrentUser();
        if (fineService.hasUnpaid(user)) {
            throw new BadRequestException("У вас есть неоплаченный штраф. Оформить новую заявку можно после его оплаты");
        }
        Book book = bookService.find(bookId);
        if (book.getType() == BookType.ORDINARY && book.getQuantityFree() <= 0) {
            throw new BadRequestException("Нет свободных экземпляров книги «" + book.getName() + "»");
        }
        save.setOwner(user);
        save.setBook(book);
        if (!discountId.equals("0")) save.setDiscount(discountService.find(discountId));
        return repository.save(save);
    }

    public BookOrdering updateDiscount(String id, String discountId) {
        BookOrdering ordering = find(id);
        if (!discountId.equals("0")) ordering.setDiscount(discountService.find(discountId));
        else ordering.setDiscount(null);
        return repository.save(ordering);
    }

    private boolean isOrdinary(BookOrdering ordering) {
        return ordering.getBook() != null && ordering.getBook().getType() == BookType.ORDINARY;
    }

    private void checkStatus(BookOrdering ordering, BookOrderingStatus... allowed) {
        for (BookOrderingStatus status : allowed) if (ordering.getStatus() == status) return;
        throw new BadRequestException("Действие недоступно для заявки в статусе «" + ordering.getStatus().getName() + "»");
    }

}
