package com.moin.transfer.exception;

import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(QuoteException.class)
    public ResponseEntity<ApiResponse<?>> handleQuoteException(QuoteException e) {
        ApiResponse<?> errorResponse = ApiResponse.builder()
                .resultCode(e.getCommonErrorCodeType().getHttpStatus().value())
                .resultMsg(e.getCommonErrorCodeType().getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, e.getCommonErrorCodeType().getHttpStatus());
    }


    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<?>> handleUserException(UserException e) {
        ApiResponse<?> errorResponse = ApiResponse.builder()
                .resultCode(e.getCommonErrorCodeType().getHttpStatus().value())
                .resultMsg(e.getCommonErrorCodeType().getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("유효성 검증에 실패했습니다.");

        ApiResponse<?> errorResponse = ApiResponse.builder()
                .resultCode(HttpStatus.BAD_REQUEST.value())
                .resultMsg(errorMessage)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}