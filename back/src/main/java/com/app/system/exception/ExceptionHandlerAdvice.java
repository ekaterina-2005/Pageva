package com.app.system.exception;

import com.app.system.Result;
import com.app.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        Map<String, String> map = new HashMap<>(errors.size());

        errors.forEach(error -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });

        return new Result(
                false,
                StatusCode.INVALID_ARGUMENT,
                "Приведенные аргументы недопустимы, подробности смотрите в разделе данные",
                map
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleObjectNotFoundException(BadRequestException e) {
        return new Result(
                false,
                StatusCode.INVALID_ARGUMENT,
                e.getMessage()
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(ObjectNotFoundException e) {
        return new Result(
                false,
                StatusCode.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception e) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "Некорректный ввод логина или пароля",
                e.getMessage()
        );
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException e) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "user account if abnormal",
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "Срок действия предоставленного токена доступа истек, он отозван, неправильно сформирован или недействителен по другим причинам",
                e.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException e) {
        return new Result(
                false,
                StatusCode.FORBIDDEN,
                "Нету доступа",
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception e) {
        return new Result(
                false,
                StatusCode.INTERNAL_SERVER_ERROR,
                "A server internal error occurs",
                e.getMessage()
        );
    }
}