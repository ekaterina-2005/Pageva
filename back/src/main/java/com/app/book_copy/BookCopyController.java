package com.app.book_copy;

import com.app.book_copy.converter.BookCopyToBookCopyDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import com.app.system.ApiExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@Tag(name = "Экземпляры")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookCopyController {

    private final BookCopyService service;
    private final BookCopyToBookCopyDtoConverter toDtoConverter;

    @Secured({MANAGER, USER})
    @Operation(
            summary = "Экземпляры книги",
            description = "Список экземпляров с инвентарными номерами и статусами: AVAILABLE (свободна), RESERVED (забронирована), "
                    + "ISSUED (выдана), RESTORATION, WRITTEN_OFF. Для выдачи выберите id экземпляра в статусе AVAILABLE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Экземпляры книги"),
            @ApiResponse(responseCode = "404", description = "Книги с таким ИД нет",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.BOOK_NOT_FOUND)))
    })
    @GetMapping("/{bookId}/copies")
    public Result findAllByBook(@Parameter(description = "ИД книги", example = "1") @PathVariable String bookId) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAllByBook(bookId).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER})
    @Operation(summary = "Добавить экземпляры (библиотекарь)", description = "Создает count экземпляров с номерами вида INV-0001-001.")
    @PostMapping("/{bookId}/copies")
    public Result save(
            @Parameter(description = "ИД книги", example = "1") @PathVariable String bookId,
            @Parameter(description = "Сколько экземпляров создать (1-1000)", example = "1") @RequestParam(defaultValue = "1") int count
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                service.save(bookId, count).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER})
    @Operation(
            summary = "Сменить статус экземпляра (библиотекарь)",
            description = "Ручная смена: AVAILABLE, RESTORATION, WRITTEN_OFF. Статусы RESERVED и ISSUED ставятся только через заявку. "
                    + "Экземпляр, который сейчас выдан или забронирован, менять нельзя."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус изменен"),
            @ApiResponse(responseCode = "409", description = "Экземпляр уже выдан читателю (или забронирован)",
                    content = @Content(examples = @ExampleObject(name = "Экземпляр в выдаче", value = ApiExamples.COPY_IN_ISSUE))),
            @ApiResponse(responseCode = "404", description = "Экземпляра с таким ИД нет",
                    content = @Content(examples = @ExampleObject(value = ApiExamples.COPY_NOT_FOUND)))
    })
    @PatchMapping("/copies/{id}/status")
    public Result updateStatus(
            @Parameter(description = "ИД экземпляра", example = "1") @PathVariable String id,
            @Parameter(description = "Новый статус", example = "RESTORATION") @RequestParam String status
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Status",
                toDtoConverter.convert(service.updateStatus(id, status))
        );
    }

    @Secured({MANAGER})
    @Operation(summary = "Удалить экземпляр без истории выдач (библиотекарь)")
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
