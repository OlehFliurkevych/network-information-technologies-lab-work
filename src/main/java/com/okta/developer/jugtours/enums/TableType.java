package com.okta.developer.jugtours.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableType {

  TWO_PERSON(0, "Table for at least 2 person and max 4 person"),
  FOUR_PERSON(1, "Table for at least 4 person and max 8 person"),
  EIGHT_PERSON(2, "Table for at least 8 person and max 12 person");

  private final Integer code;
  private final String desc;

}
