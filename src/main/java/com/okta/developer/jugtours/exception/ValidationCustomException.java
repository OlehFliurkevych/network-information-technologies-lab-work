package com.okta.developer.jugtours.exception;

import com.okta.developer.jugtours.enums.ExceptionType;
import lombok.Getter;

@Getter
public class ValidationCustomException extends AbstractCustomException {

  public ValidationCustomException(String message) {
    super(message);
  }

  @Override
  public ExceptionType getExceptionType() {
    return ExceptionType.VALIDATION;
  }

}
