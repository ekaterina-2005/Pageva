package com.app.book_ordering.converter;

import com.app.book_ordering.BookOrdering;
import com.app.book_ordering.BookOrderingDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookOrderingDtoToBookOrderingConverter implements Converter<BookOrderingDto, BookOrdering> {
    @Override
    public BookOrdering convert(BookOrderingDto source) {
        return new BookOrdering(
                source.date(),
                source.days()
        );
    }
}
