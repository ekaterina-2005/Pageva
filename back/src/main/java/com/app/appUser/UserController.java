package com.app.appUser;

import com.app.appUser.converter.UserDtoToUserConverter;
import com.app.appUser.converter.UserToUserDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserToUserDtoConverter toDtoConverter;
    private final UserDtoToUserConverter toUserConverter;

    @Secured({ADMIN})
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
    @GetMapping
    public Result find() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.getCurrentUser())
        );
    }

    @Secured({ADMIN})
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By Id",
                toDtoConverter.convert(service.find(id))
        );
    }

    @PostMapping
    public Result save(@Valid @RequestBody AppUser newUser) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(service.save(newUser))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
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
    @PatchMapping("/{id}/role")
    public Result updateRole(@PathVariable String id, @RequestParam String role) {
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
