package com.app.book_ordering;

import com.app.book_ordering.converter.BookOrderingDtoToBookOrderingConverter;
import com.app.book_ordering.converter.BookOrderingToBookOrderingDtoConverter;
import com.app.system.ApiExamples;
import com.app.system.Result;
import com.app.system.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@Tag(name = "Выдачи")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books/orderings")
public class BookOrderingController {

    private final BookOrderingService service;
    private final BookOrderingToBookOrderingDtoConverter toDtoConverter;
    private final BookOrderingDtoToBookOrderingConverter toConverter;

    @Operation(summary = "Список выдач", description = "Читатель видит только свои выдачи, библиотекарь — все.")
    @Secured({MANAGER, USER})
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
            summary = "Выдать конкретный экземпляр читателю (библиотекарь)",
            description = "Библиотекарь выбирает экземпляр (GET /books/{bookId}/copies) и читателя (GET /users/readers). "
                    + "Выдача создается сразу в статусе DONE, экземпляр получает статус ISSUED, срок возврата — сегодня + days."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Экземпляр выдан"),
            @ApiResponse(responseCode = "404", description = "Неизвестный читатель или экземпляр",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = {
                            @ExampleObject(name = "Неизвестный читатель", value = ApiExamples.READER_NOT_FOUND),
                            @ExampleObject(name = "Нет экземпляра", value = ApiExamples.COPY_NOT_FOUND)
                    })),
            @ApiResponse(responseCode = "409", description = "Экземпляр уже выдан или у читателя неоплаченный штраф за просрочку",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = {
                            @ExampleObject(name = "Экземпляр уже выдан", value = ApiExamples.COPY_ISSUED),
                            @ExampleObject(name = "Неоплаченный штраф", value = ApiExamples.UNPAID_FINE)
                    })),
            @ApiResponse(responseCode = "403", description = "Метод доступен только библиотекарю",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.FORBIDDEN)))
    })
    @Secured({MANAGER})
    @PostMapping("/issue")
    public Result issue(
            @Parameter(description = "ИД читателя", example = "3") @RequestParam String readerId,
            @Parameter(description = "ИД экземпляра", example = "3") @RequestParam String copyId,
            @Parameter(description = "Срок, дней (1-100)", example = "14") @RequestParam(defaultValue = "14") int days
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Issue",
                toDtoConverter.convert(service.issue(readerId, copyId, days))
        );
    }

    @Operation(
            summary = "Забронировать экземпляр по заявке читателя (библиотекарь)",
            description = "Для заявки, которую оформил сам читатель. Закрепляет экземпляр copyId (0 — первый свободный), "
                    + "экземпляр переходит в RESERVED, заявка — в BOOKED."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Экземпляр забронирован"),
            @ApiResponse(responseCode = "400", description = "Нет свободных экземпляров",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.NO_FREE_COPIES))),
            @ApiResponse(responseCode = "409", description = "Экземпляр уже выдан или заявка не в статусе WAITING",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = {
                            @ExampleObject(name = "Экземпляр уже выдан", value = ApiExamples.COPY_ISSUED),
                            @ExampleObject(name = "Неверный статус заявки", value = ApiExamples.WRONG_STATUS)
                    })),
            @ApiResponse(responseCode = "404", description = "Нет заявки или экземпляра",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.ORDERING_NOT_FOUND)))
    })
    @Secured({MANAGER})
    @PatchMapping("/{id}/booked")
    public Result booked(
            @Parameter(description = "ИД заявки", example = "8") @PathVariable String id,
            @Parameter(description = "ИД экземпляра (0 — первый свободный)", example = "0") @RequestParam(defaultValue = "0") String copyId
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Booked",
                toDtoConverter.convert(service.booked(id, copyId))
        );
    }

    @Operation(summary = "Отклонить заявку (библиотекарь)", description = "Забронированный экземпляр снова становится AVAILABLE.")
    @Secured({MANAGER})
    @PatchMapping("/{id}/rejected")
    public Result rejected(@Parameter(description = "ИД заявки", example = "8") @PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Rejected",
                toDtoConverter.convert(service.rejected(id))
        );
    }

    @Operation(summary = "Выдать забронированный экземпляр (библиотекарь)",
            description = "Заявка BOOKED → DONE, экземпляр RESERVED → ISSUED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Экземпляр выдан"),
            @ApiResponse(responseCode = "409", description = "Заявка не в статусе BOOKED",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.WRONG_STATUS))),
            @ApiResponse(responseCode = "404", description = "Заявки нет",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.ORDERING_NOT_FOUND)))
    })
    @Secured({MANAGER})
    @PatchMapping("/{id}/done")
    public Result done(@Parameter(description = "ИД заявки", example = "1") @PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Done",
                toDtoConverter.convert(service.done(id))
        );
    }

    @Operation(
            summary = "Возврат экземпляра (библиотекарь)",
            description = "Выдача DONE → RETURNED, экземпляр снова AVAILABLE. Если сегодня позже date + days, возврат просрочен: "
                    + "в data заполняются overdueDays и fineAmount, начисляется штраф (UNPAID), а message сообщает о просрочке. "
                    + "Пока штраф не оплачен (PATCH /fines/{id}/paid), новую выдачу этому читателю оформить нельзя (409)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Возврат принят: вовремя или с просрочкой и штрафом",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = {
                            @ExampleObject(name = "Возврат вовремя", value = ApiExamples.RETURN_ON_TIME),
                            @ExampleObject(name = "Возврат с просрочкой", value = ApiExamples.RETURN_OVERDUE)
                    })),
            @ApiResponse(responseCode = "409", description = "Выдача не в статусе DONE (например, уже возвращена)",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(name = "Повторный возврат", value = ApiExamples.WRONG_STATUS))),
            @ApiResponse(responseCode = "404", description = "Выдача не найдена",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.ORDERING_NOT_FOUND)))
    })
    @Secured({MANAGER})
    @PatchMapping("/{id}/returned")
    public Result returned(@Parameter(description = "ИД выдачи", example = "3") @PathVariable String id) {
        BookOrdering returned = service.returned(id);
        String message = returned.getFine() == null
                ? "Success Returned"
                : "Возврат просрочен на " + returned.getFine().getDaysOverdue() + " дн. Начислен штраф "
                  + returned.getFine().getAmount() + " BYN";
        return new Result(
                true,
                StatusCode.SUCCESS,
                message,
                toDtoConverter.convert(returned)
        );
    }

    @Operation(
            summary = "Оформить заявку (читатель)",
            description = "Выполняется под читателем (роль USER). В теле достаточно date (ГГГГ-ММ-ДД) и days (1-100)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples =
            @ExampleObject(value = "{\"date\": \"2026-11-12\", \"days\": 14}")))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка создана в статусе WAITING"),
            @ApiResponse(responseCode = "400", description = "Неоплаченный штраф или нет свободных экземпляров",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = {
                            @ExampleObject(name = "Неоплаченный штраф", value = ApiExamples.UNPAID_FINE_SELF),
                            @ExampleObject(name = "Нет свободных экземпляров", value = ApiExamples.NO_FREE_COPIES)
                    })),
            @ApiResponse(responseCode = "404", description = "Книги нет",
                    content = @Content(schema = @Schema(implementation = Result.class), examples = @ExampleObject(value = ApiExamples.BOOK_NOT_FOUND)))
    })
    @Secured({USER})
    @PostMapping
    public Result save(
            @Valid @RequestBody BookOrderingDto saveDto,
            @Parameter(description = "ИД книги", example = "1") @RequestParam String bookId,
            @Parameter(description = "ИД скидки (0 — без скидки)", example = "0") @RequestParam(defaultValue = "0") String discountId
    ) {
        BookOrdering save = toConverter.convert(saveDto);
        BookOrdering saved = service.save(save, bookId, discountId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Operation(summary = "Изменить скидку заявки (библиотекарь)")
    @Secured({MANAGER})
    @PatchMapping("/{id}/discount")
    public Result updateDiscount(@PathVariable String id, @RequestParam(defaultValue = "0") String discountId) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Discount",
                toDtoConverter.convert(service.updateDiscount(id, discountId))
        );
    }

}
