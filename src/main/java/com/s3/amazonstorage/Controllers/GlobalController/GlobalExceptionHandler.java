package com.s3.amazonstorage.Controllers.GlobalController;

import com.s3.amazonstorage.Exceptions.FileIsEmptyException;
import com.s3.amazonstorage.Exceptions.FileNotImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<String> FileIsEmptyException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotImageException.class)
    public ResponseEntity<String> FileIsNotImageException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
