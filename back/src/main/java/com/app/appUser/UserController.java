package com.app.appUser;

import com.app.appUser.converter.UserDtoToUserConverter;
import com.app.appUser.converter.UserToUserDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import com.app.system.ApiExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.*;

@Tag(name = "Читатели")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserToUserDtoConverter toDtoConverter;
    private final UserDtoToUserConverter toUserConverter;

    @Secured({ADMIN})
    @Operation(summary = "Все пользователи (директор)", description = "Здесь можно узнать ИД читателя для параметра readerId.")
    @GetMapping("/all")
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @Operation(summary = "Текущий пользователь", description = "Данные того, чей токен передан. Поле id — ИД читателя.")
    @GetMapping
    public Result find() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.getCurrentUser())
        );
    }

    @Operation(summary = "Поиск читателей (библиотекарь)",
            description = "Читатели (роль USER), у которых ФИО содержит query. Поле id — это readerId для выдачи.")
    @Secured({ADMIN, MANAGER})
    @GetMapping("/readers")
    public Result findReaders(
            @Parameter(description = "Часть ФИО (пусто — все читатели)", example = "Смирнова") @RequestParam(defaultValue = "") String query
    ) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find Readers",
                service.findReaders(query).stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN})
    @Operation(summary = "Пользователь по ИД (директор)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Неизвестный пользователь",
                    content = @Content(examples = @ExampleObject(value = """
                            {"flag": false, "code": 404, "message": "Не найден пользователь с ИД: 999", "data": null}""")))
    })
    @GetMapping("/{id}")
    public Result findById(@Parameter(description = "ИД пользователя", example = "3") @PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By Id",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Operation(summary = "Регистрация читателя",
            description = "Без авторизации. Новый пользователь получает роль USER (самый первый — ADMIN).")
    @SecurityRequirements
    @PostMapping
    public Result save(@Valid @RequestBody UserRegisterDto newUser) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(service.save(newUser.toUser()))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @Operation(summary = "Изменить свои данные")
    @PutMapping
    public Result update(@Valid @RequestBody UserDto updateDto) {
        AppUser update = toUserConverter.convert(updateDto);
        AppUser updated = service.update(update);
        UserDto updatedDto = toDtoConverter.convert(updated);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                updatedDto
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PatchMapping("/img")
    public Result updateImg(@RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Img",
                toDtoConverter.convert(service.updateImg(file))
        );
    }

    @Secured({ADMIN})
    @Operation(summary = "Назначить роль (директор)", description = "Роли: ADMIN, MANAGER (библиотекарь), USER (читатель).")
    @PatchMapping("/{id}/role")
    public Result updateRole(
            @Parameter(description = "ИД пользователя", example = "2") @PathVariable String id,
            @Parameter(description = "Роль", example = "MANAGER") @RequestParam String role
    ) {
        AppUser updated = service.updateRole(id, role);
        UserDto updatedDto = toDtoConverter.convert(updated);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Role",
                updatedDto
        );
    }

    @Secured({ADMIN})
    @PatchMapping("/{id}/lock")
    public Result updateLock(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Lock",
                toDtoConverter.convert(service.updateLock(id))
        );
    }

    @Secured({ADMIN})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        service.deleteById(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
