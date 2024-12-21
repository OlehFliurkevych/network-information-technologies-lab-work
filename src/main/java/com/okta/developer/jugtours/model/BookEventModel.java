package com.okta.developer.jugtours.model;

import lombok.Data;

@Data
public class BookEventModel {

  private final Long id;
  private final String userName;
  private final String date;
  private final RestaurantEntity restaurant;
  private final TableEntity table;

}
