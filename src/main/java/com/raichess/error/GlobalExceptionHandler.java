package com.raichess.error;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> api(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> body(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = e.getBindingResult().getFieldErrors().stream()
                .map(f -> Map.of("field", f.getField(), "message", String.valueOf(f.getDefaultMessage())))
                .toList();
        return invalid(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> methodValidation(HandlerMethodValidationException e) {
        List<Map<String, String>> errors = new ArrayList<>();
        for (ParameterValidationResult result : e.getAllValidationResults()) {
            if (result instanceof ParameterErrors bodyErrors) {
                bodyErrors.getFieldErrors().forEach(f ->
                        errors.add(Map.of("field", f.getField(), "message", String.valueOf(f.getDefaultMessage()))));
            } else {
                String name = String.valueOf(result.getMethodParameter().getParameterName());
                result.getResolvableErrors().forEach(err ->
                        errors.add(Map.of("field", name, "message", String.valueOf(err.getDefaultMessage()))));
            }
        }
        return invalid(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> missingParam(MissingServletRequestParameterException e) {
        return bad("Укажите " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> typeMismatch(MethodArgumentTypeMismatchException e) {
        return bad("Недопустимое значение параметра " + e.getName());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> badSort(PropertyReferenceException e) {
        return bad("Недопустимое поле сортировки: " + e.getPropertyName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> badJson(HttpMessageNotReadableException e) {
        return bad("плохой джсон айай");
    }

    private ResponseEntity<Map<String, Object>> invalid(List<Map<String, String>> errors) {
        return ResponseEntity.badRequest().body(Map.of("message", "Некорректные данные", "errors", errors));
    }

    private ResponseEntity<Map<String, Object>> bad(String message) {
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
