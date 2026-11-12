package com.app.book_copy;

import com.app.book.Book;
import com.app.book.BookRepository;
import com.app.enums.BookCopyStatus;
import com.app.enums.BookType;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ConflictException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository repository;
    private final BookRepository bookRepository;

    public List<BookCopy> findAllByBook(String bookId) {
        return repository.findAllByBookIdOrderById(findBook(bookId).getId());
    }

    public BookCopy find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден экземпляр по ИД: " + id));
    }

    @Transactional
    public List<BookCopy> save(String bookId, int count) {
        if (count < 1 || count > 1000) throw new BadRequestException("Количество экземпляров должно быть от 1 до 1000");
        Book book = findBook(bookId);
        List<BookCopy> created = createCopies(book, count);
        syncQuantity(book);
        return created;
    }

    // создает экземпляры для книги, инвентарный номер вида INV-0001-001
    public List<BookCopy> createCopies(Book book, int count) {
        if (book.getType() != BookType.ORDINARY) throw new BadRequestException("Экземпляры есть только у обычных книг");
        List<BookCopy> created = new ArrayList<>();
        int number = book.getCopies().size();
        for (int i = 0; i < count; i++) {
            String inventoryNumber;
            do {
                number++;
                inventoryNumber = String.format("INV-%04d-%03d", book.getId(), number);
            } while (repository.existsByInventoryNumber(inventoryNumber));
            BookCopy copy = repository.save(new BookCopy(inventoryNumber, book));
            book.getCopies().add(copy);
            created.add(copy);
        }
        return created;
    }

    @Transactional
    public BookCopy updateStatus(String id, String status) {
        BookCopy copy = find(id);
        BookCopyStatus newStatus;
        try {
            newStatus = BookCopyStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Некорректный статус экземпляра");
        }
        if (newStatus == BookCopyStatus.RESERVED || newStatus == BookCopyStatus.ISSUED) {
            throw new BadRequestException("Статус «" + newStatus.getName() + "» устанавливается только через заявку");
        }
        if (copy.getStatus() == BookCopyStatus.RESERVED || copy.getStatus() == BookCopyStatus.ISSUED) {
            throw new ConflictException("Экземпляр " + copy.getInventoryNumber() + " сейчас в выдаче");
        }
        copy.setStatus(newStatus);
        BookCopy saved = repository.save(copy);
        syncQuantity(copy.getBook());
        return saved;
    }

    @Transactional
    public void delete(String id) {
        BookCopy copy = find(id);
        if (!copy.getOrderings().isEmpty()) {
            throw new BadRequestException("У экземпляра " + copy.getInventoryNumber() + " есть история выдач, его можно только списать");
        }
        Book book = copy.getBook();
        book.getCopies().remove(copy);
        repository.delete(copy);
        syncQuantity(book);
    }

    public BookCopy findAvailable(Book book, String copyId) {
        if (copyId != null && !copyId.equals("0")) {
            BookCopy copy = find(copyId);
            if (!copy.getBook().getId().equals(book.getId())) {
                throw new BadRequestException("Экземпляр " + copy.getInventoryNumber() + " относится к другой книге");
            }
            if (copy.getStatus() == BookCopyStatus.ISSUED) {
                throw new ConflictException("Экземпляр " + copy.getInventoryNumber() + " уже выдан");
            }
            if (copy.getStatus() != BookCopyStatus.AVAILABLE) {
                throw new ConflictException("Экземпляр " + copy.getInventoryNumber() + " недоступен: " + copy.getStatus().getName());
            }
            return copy;
        }
        return repository.findFirstByBookIdAndStatusOrderById(book.getId(), BookCopyStatus.AVAILABLE)
                .orElseThrow(() -> new BadRequestException("Нет свободных экземпляров книги «" + book.getName() + "»"));
    }

    public BookCopy setStatus(BookCopy copy, BookCopyStatus status) {
        copy.setStatus(status);
        return repository.save(copy);
    }

    // book.quantity = число не списанных экземпляров
    public void syncQuantity(Book book) {
        book.setQuantity((int) book.getCopies().stream().filter(BookCopy::isActive).count());
        bookRepository.save(book);
    }

    private Book findBook(String bookId) {
        return bookRepository.findById(Long.parseLong(bookId)).orElseThrow(() -> new ObjectNotFoundException("Не найдена книга по ИД: " + bookId));
    }

}
