package com.app.book_comment.converter;

import com.app.book_comment.BookComment;
import com.app.book_comment.BookCommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookCommentDtoToBookCommentConverter implements Converter<BookCommentDto, BookComment> {
    @Override
    public BookComment convert(BookCommentDto source) {
        return new BookComment(
                source.score(),
                source.text()
        );
    }
}
