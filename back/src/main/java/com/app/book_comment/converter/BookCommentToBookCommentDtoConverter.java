package com.app.book_comment.converter;

import com.app.book_comment.BookComment;
import com.app.book_comment.BookCommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookCommentToBookCommentDtoConverter implements Converter<BookComment, BookCommentDto> {
    @Override
    public BookCommentDto convert(BookComment source) {
        return new BookCommentDto(
                source.getId(),

                source.getDate(),

                source.getScore(),

                source.getText(),

                source.getOwner().getId(),
                source.getOwner().getFio()
        );
    }
}
