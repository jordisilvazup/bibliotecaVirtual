package br.com.zup.edu.biblioteca.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class HandlerException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        ErrorMessage messages= new ErrorMessage();
        e.getBindingResult().getFieldErrors().forEach(messages::adicionarErrorPeloFieldError);
        return ResponseEntity.badRequest().body(messages);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<?> constraintViolationException(ConstraintViolationException e){
//        ErrorMessage messages= new ErrorMessage();
//        final String message = e.getMessage();
//        e.getBindingResult().getFieldErrors().forEach(messages::adicionarErrorPeloFieldError);
//        return ResponseEntity.badRequest().body(messages);
//    }

}
