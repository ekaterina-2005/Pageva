package com.app.book;

import com.app.book.converter.BookDtoToBookConverter;
import com.app.book.converter.BookToBookDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.app.system.ApiExamples;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;

@Tag(name = "Книги")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService service;
    private final BookToBookDtoConverter toDtoConverter;
    private final BookDtoToBookConverter toConverter;

    @Operation(summary = "Список всех книг", description = "Доступно без авторизации. В data — массив BookDto вместе с экземплярами (copies).")
    @SecurityRequirements
    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Поиск книги",
            description = "Ищет книги по части названия или автора без учета регистра. "
                    + "В ответе у каждой книги есть quantityFree и список copies: id экземпляра со статусом AVAILABLE "
                    + "используется дальше как copyId при выдаче."
    )
    @ApiResponse(responseCode = "200", description = "Найденные книги (пустой массив, если ничего не найдено)")
    @SecurityRequirements
    @GetMapping("/search")
    public Result search(
            @Parameter(description = "Часть названия или автора", example = "Война") @RequestParam(defaultValue = "") String query,
            @Parameter(description = "true — только книги со свободными экземплярами", example = "false") @RequestParam(defaultValue = "false") boolean available
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Search",
                service.search(query, available).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Operation(summary = "Книга по ИД", description = "Карточка книги с экземплярами, отзывами и заявками.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книга найдена"),
            @ApiResponse(responseCode = "404", description = "Книги с таким ИД нет",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.BOOK_NOT_FOUND)))
    })
    @SecurityRequirements
    @GetMapping("/{id}")
    public Result find(@Parameter(description = "ИД книги", example = "1") @PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Secured({MANAGER})
    @Operation(summary = "Добавить книгу (библиотекарь)",
            description = "Для обычной книги (type = ORDINARY) сразу создается quantity экземпляров с инвентарными номерами.")
    @PostMapping
    public Result save(
            @Valid @RequestBody BookDto saveDto,
            @Parameter(description = "ИД категории", example = "1") @RequestParam String categoryId,
            @Parameter(description = "Возрастная категория (A0_10, A11_15, A16_20, A21)", example = "A16_20") @RequestParam String age,
            @Parameter(description = "Тип книги (ORDINARY, ELECTRONIC)", example = "ORDINARY") @RequestParam String type
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
    @Operation(summary = "Изменить книгу (библиотекарь)")
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
    @Operation(summary = "Удалить книгу (библиотекарь)")
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
