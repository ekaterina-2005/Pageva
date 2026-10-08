package com.app.book_comment;

import com.app.book_comment.converter.BookCommentDtoToBookCommentConverter;
import com.app.book_comment.converter.BookCommentToBookCommentDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/comments")
@Secured({USER})
public class BookCommentController {

    private final BookCommentService service;
    private final BookCommentToBookCommentDtoConverter toDtoConverter;
    private final BookCommentDtoToBookCommentConverter toConverter;

    @PostMapping
    public Result save(@Valid @RequestBody BookCommentDto saveDto, @RequestParam String bookId) {
        BookComment save = toConverter.convert(saveDto);
        BookComment saved = service.save(save, bookId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @Valid @RequestBody BookCommentDto updateDto) {
        BookComment update = toConverter.convert(updateDto);
        BookComment updated = service.update(id, update);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

}
