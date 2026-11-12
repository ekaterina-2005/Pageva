package com.app.fine;

import com.app.fine.converter.FineToFineDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.ADMIN;
import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@Tag(name = "Штрафы")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fines")
public class FineController {

    private final FineService service;
    private final FineToFineDtoConverter toDtoConverter;

    @Secured({ADMIN, MANAGER, USER})
    @Operation(summary = "Список штрафов", description = "Читатель видит свои штрафы, библиотекарь — все.")
    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER, USER})
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
    @Operation(summary = "Отметить штраф оплаченным (библиотекарь)",
            description = "После оплаты читателю снова можно выдавать книги.")
    @PatchMapping("/{id}/paid")
    public Result pay(@Parameter(description = "ИД штрафа", example = "1") @PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Paid",
                toDtoConverter.convert(service.pay(id))
        );
    }

    @Secured({MANAGER})
    @Operation(summary = "Аннулировать штраф (библиотекарь)")
    @PatchMapping("/{id}/cancel")
    public Result cancel(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Cancel",
                toDtoConverter.convert(service.cancel(id))
        );
    }

}
