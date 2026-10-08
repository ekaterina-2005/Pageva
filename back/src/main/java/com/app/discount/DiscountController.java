package com.app.discount;

import com.app.discount.converter.DiscountDtoToDiscountConverter;
import com.app.discount.converter.DiscountToDiscountDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService service;
    private final DiscountToDiscountDtoConverter toDtoConverter;
    private final DiscountDtoToDiscountConverter toConverter;

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
    @Secured({ADMIN})
    public Result save(@Valid @RequestBody DiscountDto saveDto) {
        Discount save = toConverter.convert(saveDto);
        Discount saved = service.save(save);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @PutMapping("/{id}")
    @Secured({ADMIN})
    public Result update(@PathVariable String id, @Valid @RequestBody DiscountDto updateDto) {
        Discount update = toConverter.convert(updateDto);
        Discount updated = service.update(id, update);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @DeleteMapping("/{id}")
    @Secured({ADMIN})
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
