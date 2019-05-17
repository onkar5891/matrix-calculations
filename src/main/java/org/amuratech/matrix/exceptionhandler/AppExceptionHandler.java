package org.amuratech.matrix.exceptionhandler;

import org.amuratech.matrix.datatransfer.ApiResponse;
import org.amuratech.matrix.exception.InvalidInputException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(InvalidInputException e) {
        ApiResponse response = new ApiResponse("Encountered error in request body", Collections.singletonList(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + " " + error.getDefaultMessage());
        }
        ApiResponse error = new ApiResponse("Validation Failed", details);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
