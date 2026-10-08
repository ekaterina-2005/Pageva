package com.app.book.converter;

import com.app.book.Book;
import com.app.book.BookDto;
import com.app.book_comment.converter.BookCommentToBookCommentDtoConverter;
import com.app.book_ordering.converter.BookOrderingToBookOrderingDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookToBookDtoConverter implements Converter<Book, BookDto> {

    private final BookCommentToBookCommentDtoConverter bookCommentToBookCommentDtoConverter;
    private final BookOrderingToBookOrderingDtoConverter bookOrderingToBookOrderingDtoConverter;

    @Override
    public BookDto convert(Book source) {
        return new BookDto(
                source.getId(),

                source.getName(),
                source.getAuthor(),
                source.getPage(),
                source.getDate(),
                source.getRead(),
                source.getPrice(),
                source.getQuantity(),

                source.getQuantityFree(),

                source.isRepair(),
                source.isVisibility(),

                source.getDescription(),

                source.getImg(),
                source.getFile(),

                source.getAge().name(),
                source.getAge().getName(),

                source.getType().name(),
                source.getType().getName(),

                source.getCategory().getId(),
                source.getCategory().getName(),

                source.getCount(),

                source.getScore(),

                source.getComments().stream().map(bookCommentToBookCommentDtoConverter::convert).collect(Collectors.toList()),
                source.getOrderings().stream().map(bookOrderingToBookOrderingDtoConverter::convert).collect(Collectors.toList())
        );
    }
}
