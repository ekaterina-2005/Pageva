package com.app.book_ordering;

import com.app.book_ordering.converter.BookOrderingDtoToBookOrderingConverter;
import com.app.book_ordering.converter.BookOrderingToBookOrderingDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;
import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/orderings")
public class BookOrderingController {

    private final BookOrderingService service;
    private final BookOrderingToBookOrderingDtoConverter toDtoConverter;
    private final BookOrderingDtoToBookOrderingConverter toConverter;

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

    @Secured({MANAGER})
    @GetMapping("/{id}/booked")
    public Result booked(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Booked",
                toDtoConverter.convert(service.booked(id))
        );
    }

    @Secured({MANAGER})
    @GetMapping("/{id}/rejected")
    public Result rejected(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Rejected",
                toDtoConverter.convert(service.rejected(id))
        );
    }


    @Secured({MANAGER})
    @GetMapping("/{id}/done")
    public Result done(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Done",
                toDtoConverter.convert(service.done(id))
        );
    }

    @Secured({MANAGER})
    @GetMapping("/{id}/returned")
    public Result returned(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Returned",
                toDtoConverter.convert(service.returned(id))
        );
    }

    @Secured({USER})
    @PostMapping
    public Result save(
            @Valid @RequestBody BookOrderingDto saveDto, @RequestParam String bookId,
            @RequestParam(defaultValue = "0") String discountId
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
