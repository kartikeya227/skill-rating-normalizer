package casa2.assignment.ratingnormalizer.controller;

import casa2.assignment.ratingnormalizer.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleAnyException(Exception ex) {
        return ResponseHandler.generateResponse("Exception occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}