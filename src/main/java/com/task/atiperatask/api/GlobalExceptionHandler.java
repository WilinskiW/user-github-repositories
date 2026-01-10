package com.task.atiperatask.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper mapper;

    GlobalExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        Map<String, Object> body = new HashMap<>();

        JsonNode node = mapper.readTree(ex.getResponseBodyAsString());
        String errorMsg = node.path("message").asString();

        body.put("status", ex.getStatusCode().value());
        body.put("message", errorMsg);

        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}
