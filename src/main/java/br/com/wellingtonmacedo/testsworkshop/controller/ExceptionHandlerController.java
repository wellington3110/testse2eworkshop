package br.com.wellingtonmacedo.testsworkshop.controller;

import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;
import br.com.wellingtonmacedo.testsworkshop.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDTO> validationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(exception.getError());
    }
}
