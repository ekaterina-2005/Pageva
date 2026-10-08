package com.app.book_comment;

import com.app.appUser.UserService;
import com.app.book.BookService;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentService {

    private final BookCommentRepository repository;
    private final BookService bookService;
    private final UserService userService;

    public BookComment find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден комментарий по ИД: " + id));
    }

    public BookComment save(BookComment save, String bookId) {
        save.setOwner(userService.getCurrentUser());
        save.setBook(bookService.find(bookId));
        return repository.save(save);
    }

    public BookComment update(String id, BookComment update) {
        BookComment old = find(id);
        old.update(update);
        return repository.save(old);
    }

}
