package com.app.subscription_ordering;

import com.app.subscription_ordering.converter.SubscriptionOrderingDtoToSubscriptionOrderingConverter;
import com.app.subscription_ordering.converter.SubscriptionOrderingToSubscriptionOrderingDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.app.util.Global.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions/orderings")
@Secured({USER})
public class SubscriptionOrderingController {

    private final SubscriptionOrderingService service;
    private final SubscriptionOrderingDtoToSubscriptionOrderingConverter toConverter;
    private final SubscriptionOrderingToSubscriptionOrderingDtoConverter toDtoConverter;

    @GetMapping("/all")
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/active")
    public Result active() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Active",
                service.isActive()
        );
    }

    @PostMapping
    public Result save(@Valid @RequestBody SubscriptionOrderingDto saveDto, @RequestParam String subscriptionId) {
        SubscriptionOrdering save = toConverter.convert(saveDto);
        SubscriptionOrdering saved = service.save(save, subscriptionId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

}
