package com.okta.developer.jugtours.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.okta.developer.jugtours.enums.TableType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tables")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableEntity {

  @Id
  @GeneratedValue
  private Long id;
  @Enumerated(EnumType.STRING)
  private TableType tableType;
  @Column(columnDefinition = "integer default 0")
  private Integer count;
  @ManyToOne
  @JoinColumn(name = "restaurant_id")
  @JsonIgnore
  private RestaurantEntity restaurant;
  @OneToMany(mappedBy = "table")
  @JsonIgnore
  private List<BookEventEntity> bookEvents;

}
