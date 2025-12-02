package br.gov.sp.fatec.controllers.advices;

import br.gov.sp.fatec.dtos.ErrorResponseDto;
import br.gov.sp.fatec.exceptions.BadCredentialsException;
import br.gov.sp.fatec.exceptions.NonCompliantPasswordException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, List<String>> errorsMessagePerField = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        Collectors.toList()
                    )
            )
        );

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            req.getRequestURI(),
            "Validation failed",
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            Instant.now(),
            errorsMessagePerField
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest req) {
       var dto = new ErrorResponseDto(
               req.getRequestURI(),
               ex.getMessage(),
               HttpStatus.UNAUTHORIZED.value(),
               HttpStatus.UNAUTHORIZED.getReasonPhrase(),
               Instant.now(),
               null
       );

         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(NonCompliantPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleNonCompliantPasswordException(NonCompliantPasswordException ex, HttpServletRequest req) {
        var dto = new ErrorResponseDto(
                req.getRequestURI(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, HttpServletRequest req) {
        var dto = new ErrorResponseDto(
                req.getRequestURI(),
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}