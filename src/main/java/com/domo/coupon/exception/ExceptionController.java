package com.domo.coupon.exception;

import com.domo.coupon.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFountException(UserNotFoundException e, HttpServletRequest request) {
        log.warn("Exception url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = new ErrorResponse("UserNotFound", e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<ErrorResponse> DuplicateIdExceptionException(DuplicateIdException e, HttpServletRequest request) {
        log.warn("Exception url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = new ErrorResponse("DuplicateIdException", e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        String code = bindingResult.getFieldError().getCode();
        String message = bindingResult.getFieldError().getDefaultMessage();
        return new ErrorResponse(code, message);
    }

}
