package com.accenture.controller.advice;

import com.accenture.exception.IngredientException;
import com.accenture.exception.PizzaException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {

    @ExceptionHandler(IngredientException.class)
    public ResponseEntity<MessageError> handleIngredientException(IngredientException e){
        MessageError me = new MessageError(LocalDateTime.now(), "Erreur validation", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(me);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageError> problemeValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.joining(","));
        MessageError er = new MessageError(LocalDateTime.now(),"Erreur validation", message);
        log.error("Erreur de validation");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageError> entityNotFoundException(EntityNotFoundException ex) {
        MessageError messageError= new MessageError(LocalDateTime.now(),"L'id n'existe pas" , ex.getMessage());
        log.error("Mauvaise requête");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageError);
    }
    @ExceptionHandler(PizzaException.class)
    public ResponseEntity<String> pizzaException(PizzaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
