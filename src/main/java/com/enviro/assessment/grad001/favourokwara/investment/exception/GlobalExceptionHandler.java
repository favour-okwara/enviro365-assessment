package com.enviro.assessment.grad001.favourokwara.investment.exception;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handle(ApplicationException ex,
                                                      WebRequest request) {
        return getErrorsMap(request, ex.getErrorResponse().getHttpStatus(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidArgumentsException(
        MethodArgumentNotValidException ex,
        WebRequest request
    ) {
        List<ConstraintsViolationError> errors = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        return getErrorsMap(request, HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    }


    protected ResponseEntity<Map<String, Object>> getErrorsMap(WebRequest request,
                                                         HttpStatus status, ApplicationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        return getErrorsMap(request, status, ex.getLocalizedMessage(), List.of());
    }

    private ResponseEntity<Map<String, Object>> getErrorsMap(WebRequest request, HttpStatus status, String message, List<ConstraintsViolationError> errors) {
        Map<String, Object> errorResponse = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        errorResponse.put(HttpResponseConstants.STATUS, status.value());
        errorResponse.put(HttpResponseConstants.ERROR, status);
        errorResponse.put(HttpResponseConstants.MESSAGE, message);
        errorResponse.put(HttpResponseConstants.ERRORS, errors);
        errorResponse.put(HttpResponseConstants.PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }

}
