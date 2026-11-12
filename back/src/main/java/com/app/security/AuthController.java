package com.app.security;

import com.app.system.Result;
import com.app.system.StatusCode;
import com.app.system.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Читатели")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Вход: получить JWT",
            description = "Перед вызовом нажмите Authorize и заполните basicAuth (логин/пароль). "
                    + "Токен из data.token вставьте в bearerAuth — он нужен для остальных методов.",
            security = @SecurityRequirement(name = OpenApiConfig.BASIC))
    @PostMapping("/login")
    public Result getLoginIndo(@Parameter(hidden = true) Authentication authentication) {
        LOGGER.debug("Authenticated user: '{}'", authentication.getName());
        return new Result(
                true,
                StatusCode.SUCCESS,
                "User Info and JSON Web Token",
                authService.createLoginInfo(authentication)
        );
    }

}