package com.app.book.converter;

import com.app.book.Book;
import com.app.book.BookDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.app.util.Global.round;

@Component
public class BookDtoToBookConverter implements Converter<BookDto, Book> {
    @Override
    public Book convert(BookDto source) {
        return new Book(
                source.name(),
                source.author(),
                source.page(),
                source.date(),
                source.read(),
                round(source.price()),
                source.quantity(),
                source.description()
        );
    }
}
