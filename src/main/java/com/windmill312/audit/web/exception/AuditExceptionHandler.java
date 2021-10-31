package com.windmill312.audit.web.exception;

import com.windmill312.audit.exception.InsufficientCountryCodeFormatException;
import com.windmill312.audit.web.dto.ErrorResponse;
import com.windmill312.audit.web.dto.StatusfulResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@ControllerAdvice
public class AuditExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<StatusfulResponse> onInsufficientCountryCodeFormatException(InsufficientCountryCodeFormatException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler
    public ResponseEntity<StatusfulResponse> onRuntimeException(RuntimeException e) {
        log.error("RuntimeException: {}, {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).build());
    }
}