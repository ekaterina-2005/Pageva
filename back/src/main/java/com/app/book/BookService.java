package com.app.book;

import com.app.book_copy.BookCopy;
import com.app.book_copy.BookCopyService;
import com.app.category.CategoryService;
import com.app.enums.BookAge;
import com.app.enums.BookType;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.app.util.Global.saveFile;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final CategoryService categoryService;
    private final BookCopyService bookCopyService;

    public List<Book> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Book> findAllByAge(BookAge age) {
        return repository.findAllByAge(age);
    }

    public Book find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена книга по ИД: " + id));
    }

    // для обычной книги сразу создаются quantity экземпляров
    @Transactional
    public Book save(Book save, String categoryId, String age, String type) {
        setAge(save, age);
        setType(save, type);
        save.setCategory(categoryService.find(categoryId));
        Book saved = repository.save(save);
        if (saved.getType() == BookType.ORDINARY) {
            if (saved.getQuantity() > 0) bookCopyService.createCopies(saved, saved.getQuantity());
            bookCopyService.syncQuantity(saved);
        }
        return saved;
    }

    public Book saveForTest(Book save) {
        return repository.save(save);
    }

    // увеличение quantity досоздаёт экземпляры, уменьшение — только через списание экземпляров
    @Transactional
    public Book update(String id, Book update, String categoryId, String age, String type) {
        Book old = find(id);
        old.update(update);
        setAge(old, age);
        setType(old, type);
        old.setCategory(categoryService.find(categoryId));
        if (old.getType() == BookType.ORDINARY) {
            int active = (int) old.getCopies().stream().filter(BookCopy::isActive).count();
            if (update.getQuantity() < active) {
                throw new BadRequestException("Чтобы уменьшить количество, спишите лишние экземпляры на странице книги");
            }
            Book saved = repository.save(old);
            if (update.getQuantity() > active) bookCopyService.createCopies(saved, update.getQuantity() - active);
            bookCopyService.syncQuantity(saved);
            return saved;
        }
        return repository.save(old);
    }

    public Book updateForTest(String id, Book update) {
        Book old = find(id);
        old.update(update);
        return repository.save(old);
    }

    public Book updateImg(String id, MultipartFile img) {
        Book book = find(id);
        try {
            book.setImg(saveFile(img, "book"));
        } catch (IOException e) {
            throw new BadRequestException("Некорректное изображение");
        }
        return repository.save(book);
    }

    public Book updateFile(String id, MultipartFile file) {
        Book book = find(id);
        try {
            book.setFile(saveFile(file, "book"));
        } catch (IOException e) {
            throw new BadRequestException("Некорректный файл");
        }
        return repository.save(book);
    }

    public Book updateRepair(String id) {
        Book book = find(id);
        book.setRepair(!book.isRepair());
        return repository.save(book);
    }

    public Book visibility(String id) {
        Book book = find(id);
        book.setVisibility(!book.isVisibility());
        return repository.save(book);
    }

    public void delete(String id) {
        repository.deleteById(find(id).getId());
    }

    private void setAge(Book book, String age) {
        try {
            book.setAge(BookAge.valueOf(age));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Некорректный выбор возрастной категории");
        }
    }

    private void setType(Book book, String type) {
        try {
            book.setType(BookType.valueOf(type));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Некорректный выбор возрастной категории");
        }
    }

}
