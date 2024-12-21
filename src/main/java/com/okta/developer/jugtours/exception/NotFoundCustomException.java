package com.okta.developer.jugtours.exception;

import com.okta.developer.jugtours.enums.ExceptionType;
import lombok.Getter;

@Getter
public class NotFoundCustomException extends AbstractCustomException {

  public NotFoundCustomException(String message) {
    super(message);
  }

  @Override
  public ExceptionType getExceptionType() {
    return ExceptionType.NOT_FOUND;
  }
}
