package com.app.book_copy;

import com.app.book_copy.converter.BookCopyToBookCopyDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookCopyController {

    private final BookCopyService service;
    private final BookCopyToBookCopyDtoConverter toDtoConverter;

    @Secured({MANAGER, USER})
    @GetMapping("/{bookId}/copies")
    public Result findAllByBook(@PathVariable String bookId) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAllByBook(bookId).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER})
    @PostMapping("/{bookId}/copies")
    public Result save(@PathVariable String bookId, @RequestParam(defaultValue = "1") int count) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                service.save(bookId, count).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/copies/{id}/status")
    public Result updateStatus(@PathVariable String id, @RequestParam String status) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Status",
                toDtoConverter.convert(service.updateStatus(id, status))
        );
    }

    @Secured({MANAGER})
    @DeleteMapping("/copies/{id}")
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
