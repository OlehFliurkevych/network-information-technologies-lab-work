package com.okta.developer.jugtours.exception;

import com.okta.developer.jugtours.enums.ExceptionType;
import lombok.Getter;

@Getter
public abstract class AbstractCustomException extends RuntimeException {

  public AbstractCustomException(final String message) {
    super(message);
  }

  public abstract ExceptionType getExceptionType();

}
