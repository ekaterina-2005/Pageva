package com.app.subscription;

import com.app.subscription.converter.SubscriptionDtoToSubscriptionConverter;
import com.app.subscription.converter.SubscriptionToSubscriptionDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;
    private final SubscriptionDtoToSubscriptionConverter toConverter;
    private final SubscriptionToSubscriptionDtoConverter toDtoConverter;

    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public Result find(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Secured({ADMIN})
    @PostMapping
    public Result save(@RequestBody SubscriptionDto saveDto) {
        Subscription save = toConverter.convert(saveDto);
        Subscription saved = service.save(save);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Secured({ADMIN})
    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @RequestBody SubscriptionDto updateDto) {
        Subscription update = toConverter.convert(updateDto);
        Subscription updated = service.update(id, update);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @Secured({ADMIN})
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
