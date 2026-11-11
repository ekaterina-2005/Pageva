package com.app.book_copy.converter;

import com.app.book_copy.BookCopy;
import com.app.book_copy.BookCopyDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookCopyToBookCopyDtoConverter implements Converter<BookCopy, BookCopyDto> {
    @Override
    public BookCopyDto convert(BookCopy source) {
        return new BookCopyDto(
                source.getId(),

                source.getInventoryNumber(),

                source.getStatus().name(),
                source.getStatus().getName(),

                source.getDateAdded() == null ? "" : source.getDateAdded().toString(),

                source.getBook().getId(),
                source.getBook().getName(),

                source.getOrderings().size()
        );
    }
}
