package com.okta.developer.jugtours.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

  ADMIN(0, "Administrator"),
  REGULAR(1, "Regular user");

  private final Integer userTypeCode;
  private final String description;

}
