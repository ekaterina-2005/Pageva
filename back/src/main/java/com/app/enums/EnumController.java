package com.app.enums;

import com.app.system.Result;
import com.app.system.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/enums")
public class EnumController {

    @GetMapping("/roles")
    public Result getRoles() {
        Map<String, String> res = new HashMap<>();
        for (Role i : Role.values()) res.put(i.name(), i.getName());
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Roles",
                res
        );
    }

    @GetMapping("/bookAges")
    public Result getBookAges() {
        Map<String, String> res = new HashMap<>();
        for (BookAge i : BookAge.values()) res.put(i.name(), i.getName());
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Book Ages",
                res
        );
    }

    @GetMapping("/book/types")
    public Result bookTypes() {
        Map<String, String> res = new HashMap<>();
        for (BookType i : BookType.values()) res.put(i.name(), i.getName());
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Book Types",
                res
        );
    }

}
