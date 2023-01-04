package com.attornatus.proj.api.exceptionhandler;

import java.time.OffsetDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.attornatus.proj.domain.exception.PessoaNaoEncontradaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(PessoaNaoEncontradaException ex, WebRequest request) {
        var problema = new Problema();
        HttpStatus status = HttpStatus.NOT_FOUND;
        problema.setStatus(status.value());
        problema.setDataHora(OffsetDateTime.now());
        problema.setTitulo(ex.getMessage());
        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }
}
