package com.example.Pill_Mate_Backend.global.common.exception;

import com.example.Pill_Mate_Backend.global.common.code.ErrorReasonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorReasonDTO> handleGeneralException(GeneralException ex) {
        ErrorReasonDTO error = ex.getErrorReasonHttpStatus();
        return ResponseEntity
                .status(error.httpStatus())
                .body(error);
    }
}