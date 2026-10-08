package com.app.book;

import com.app.book.converter.BookDtoToBookConverter;
import com.app.book.converter.BookToBookDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService service;
    private final BookToBookDtoConverter toDtoConverter;
    private final BookDtoToBookConverter toConverter;

    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public Result find(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Secured({MANAGER})
    @PostMapping
    public Result save(
            @Valid @RequestBody BookDto saveDto, @RequestParam String categoryId, @RequestParam String age,
            @RequestParam String type
    ) {
        Book save = toConverter.convert(saveDto);
        Book saved = service.save(save, categoryId, age, type);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Secured({MANAGER})
    @PutMapping("/{id}")
    public Result update(
            @PathVariable String id, @Valid @RequestBody BookDto updateDto, @RequestParam String categoryId,
            @RequestParam String age, @RequestParam String type
    ) {
        Book update = toConverter.convert(updateDto);
        Book updated = service.update(id, update, categoryId, age, type);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/img")
    public Result updateImg(@PathVariable String id, @RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Img",
                toDtoConverter.convert(service.updateImg(id, file))
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/file")
    public Result updateFile(@PathVariable String id, @RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update File",
                toDtoConverter.convert(service.updateFile(id, file))
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/repair")
    public Result updateRepair(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Repair",
                toDtoConverter.convert(service.updateRepair(id))
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/visibility")
    public Result visibility(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Visibility",
                toDtoConverter.convert(service.visibility(id))
        );
    }

    @Secured({MANAGER})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
