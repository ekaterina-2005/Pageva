package com.app.book_ordering.converter;

import com.app.book_ordering.BookOrdering;
import com.app.book_ordering.BookOrderingDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookOrderingToBookOrderingDtoConverter implements Converter<BookOrdering, BookOrderingDto> {
    @Override
    public BookOrderingDto convert(BookOrdering source) {
        return new BookOrderingDto(
                source.getId(),

                source.getDate(),
                source.getDays(),

                source.getDateStart(),
                source.getDateEnd(),

                source.getPercent(),
                source.getPrice(),
                source.getSum(),

                source.getStatus().name(),
                source.getStatus().getName(),

                source.getBook().getId(),
                source.getBook().getName(),
                source.getBook().getImg(),

                source.getDiscountId(),
                source.getDiscountName(),

                source.getOwner().getId(),
                source.getOwner().getFio()
        );
    }
}
