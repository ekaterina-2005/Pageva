package com.app.category;

import com.app.category.converter.CategoryDtoToCategoryConverter;
import com.app.category.converter.CategoryToCategoryDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;
    private final CategoryToCategoryDtoConverter toDtoConverter;
    private final CategoryDtoToCategoryConverter toConverter;

    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @PostMapping
    @Secured({MANAGER})
    public Result save(@Valid @RequestBody CategoryDto saveDto) {
        Category save = toConverter.convert(saveDto);
        Category saved = service.save(save);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @PutMapping("/{id}")
    @Secured({MANAGER})
    public Result update(@PathVariable String id, @Valid @RequestBody CategoryDto updateDto) {
        Category update = toConverter.convert(updateDto);
        Category updated = service.update(id, update);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @DeleteMapping("/{id}")
    @Secured({MANAGER})
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
