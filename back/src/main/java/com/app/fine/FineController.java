package com.app.fine;

import com.app.fine.converter.FineToFineDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.ADMIN;
import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fines")
public class FineController {

    private final FineService service;
    private final FineToFineDtoConverter toDtoConverter;

    @Secured({ADMIN, MANAGER, USER})
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
    @PatchMapping("/{id}/paid")
    public Result pay(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Paid",
                toDtoConverter.convert(service.pay(id))
        );
    }

    @Secured({MANAGER})
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
