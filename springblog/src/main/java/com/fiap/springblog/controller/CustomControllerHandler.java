package com.fiap.springblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerHandler {

    //uso da classe MethodArgumentNotValidException.class para tratar exceções
    // esse método vai retornar o campo e o erro . Coleta os erros e retorna numa lista

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex){
        List<String> errors
                = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField()
                + ": " + e.getDefaultMessage()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
