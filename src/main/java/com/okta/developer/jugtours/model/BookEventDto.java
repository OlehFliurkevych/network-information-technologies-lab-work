package com.okta.developer.jugtours.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEventDto {

  private Long id;
  private Long restaurant;
  private Long table;
  private Date date;
  private String time;

}
