package com.okta.developer.jugtours.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  protected ErrorResponse handleServiceException(final Exception e) {
    log.error(e.getMessage(), e);
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
  }

  @ExceptionHandler(value = ValidationCustomException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleCustomerAlreadyExistsException(final ValidationCustomException e) {
    log.error(e.getMessage(), e);
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler(value = NotFoundCustomException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleCustomerAlreadyExistsException(final NotFoundCustomException e) {
    log.error(e.getMessage(), e);
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
  }

}
