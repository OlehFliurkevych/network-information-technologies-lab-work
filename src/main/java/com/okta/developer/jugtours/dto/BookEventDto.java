package com.okta.developer.jugtours.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;

@Data
@AllArgsConstructor
public class BookEventDto {

  private final Long userId;
  private final Long tableId;
  private final Date date;
  private final Time time;

}
