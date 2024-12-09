package com.okta.developer.jugtours.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.okta.developer.jugtours.enums.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @Id
  private String id;
  private String name;
  private String email;
  @Enumerated(EnumType.STRING)
  private UserType userType;
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<RestaurantEntity> restaurants;
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<BookEventEntity> bookEvents;

}
