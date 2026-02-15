package com.task.atiperatask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestControllerAdvice
class GlobalExceptionHandler {
    private final ObjectMapper mapper;

    @Value("${error.media.not-acceptable}")
    private String notAcceptableErrorMsg;

    GlobalExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<UserNotFoundErrorDto> handleHttpClientErrorException(HttpClientErrorException ex) {
        JsonNode errorNode = mapper.readTree(ex.getResponseBodyAsString());
        String githubErrorMsg = errorNode.path("message").asString();

        var errorBody = new UserNotFoundErrorDto(ex.getStatusCode().value(), githubErrorMsg);

        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ResponseEntity<MediaNotAcceptableErrorDto> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        var errorBody = new MediaNotAcceptableErrorDto(ex.getStatusCode().value(), notAcceptableErrorMsg);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorBody);
    }
}
