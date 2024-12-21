package com.okta.developer.jugtours.web;

import com.okta.developer.jugtours.enums.TableType;
import com.okta.developer.jugtours.exception.ValidationCustomException;
import com.okta.developer.jugtours.model.TableEntity;
import com.okta.developer.jugtours.repository.BookEventRepository;
import com.okta.developer.jugtours.repository.RestaurantRepository;
import com.okta.developer.jugtours.repository.TableRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tables")
public class TableController {

  private final Logger log = LoggerFactory.getLogger(TableController.class);
  private final TableRepository tableRepository;
  private final RestaurantRepository restaurantRepository;
  private final BookEventRepository bookEventRepository;

  public TableController(TableRepository tableRepository,
    RestaurantRepository restaurantRepository,
    BookEventRepository bookEventRepository) {
    this.tableRepository = tableRepository;
    this.restaurantRepository = restaurantRepository;
    this.bookEventRepository = bookEventRepository;
  }

  @GetMapping
  public List<TableEntity> tables(@RequestParam("restaurantId") Long restaurantId) {
    return tableRepository.findAllByRestaurantId(restaurantId);
  }

  @GetMapping("/{id}")
  ResponseEntity<?> getTable(@RequestParam("restaurantId") Long restaurantId,
    @PathVariable Long id) {
    Optional<TableEntity> group = tableRepository.findById(id);
    return group.map(response -> ResponseEntity.ok().body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  ResponseEntity<TableEntity> createTable(@Valid @RequestBody TableEntity table,
    @RequestParam("restaurantId") Long restaurantId) throws URISyntaxException {
    log.info("Request to create table: {}", table);

    var restaurant = restaurantRepository.findById(restaurantId).get();

    if (restaurant.getTables().size() >= TableType.values().length) {
      throw new ValidationCustomException(
        String.format("Max number of table types reached: %d. Posible table types: [%s]",
          TableType.values().length, List.of(TableType.values())));
    }

    final var hasTheSameTableType = restaurant.getTables().stream()
      .anyMatch(tableEntity -> tableEntity.getTableType().equals(table.getTableType()));
    if (hasTheSameTableType) {
      throw new ValidationCustomException(
        String.format("This restaurant '%s' have already had table with the same table type '%s' "
                      + "Please update existing or remove and create new one.",
          restaurant.getName(), table.getTableType()));
    }

    table.setRestaurant(restaurant);
    TableEntity result = tableRepository.save(table);
    return ResponseEntity.created(
      new URI("/api/tables/" + result.getId() + "?restaurantId=" + restaurantId)).body(result);
  }

  @PutMapping
  ResponseEntity<TableEntity> updateTable(@Valid @RequestBody TableEntity table,
    @RequestParam("restaurantId") Long restaurantId) {
    log.info("Request to update table: {}", table);
    TableEntity result = tableRepository.save(table);
    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTable(@PathVariable Long id) {
    log.info("Request to delete table: {}", id);

    final var table = tableRepository.findById(id).get();
    final var events = bookEventRepository.findAllByTableId(id);
    if (!events.isEmpty()) {
      throw new ValidationCustomException(String.format(
        "Table '%s' can't be deleted. This table used in bookings.", table.getTableType().name()));
    }

    tableRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
