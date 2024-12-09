package com.okta.developer.jugtours;

import com.okta.developer.jugtours.enums.TableType;
import com.okta.developer.jugtours.model.BookEventEntity;
import com.okta.developer.jugtours.model.TableEntity;
import com.okta.developer.jugtours.repository.BookEventRepository;
import com.okta.developer.jugtours.repository.RestaurantRepository;
import com.okta.developer.jugtours.repository.TableRepository;
import com.okta.developer.jugtours.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
class Initializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final TableRepository tableRepository;
  private final BookEventRepository bookEventRepository;

  public Initializer(UserRepository userRepository, RestaurantRepository restaurantRepository,
    TableRepository tableRepository, BookEventRepository bookEventRepository) {
    this.userRepository = userRepository;
    this.restaurantRepository = restaurantRepository;
    this.tableRepository = tableRepository;
    this.bookEventRepository = bookEventRepository;
  }

  @Override
  public void run(String... strings) {
    final var fliurkevych = userRepository.findById("00uk101uwjApGihhC5d7");
    final var fliurole = userRepository.findById("00ulnkyqli12OE1075d7");

    final var restaurant1 = restaurantRepository.findById(102L).get();
    final var rest2 = restaurantRepository.findById(103L).get();

    final var rest6 = restaurantRepository.findById(107L).get();
    final var rest7 = restaurantRepository.findById(108L).get();

    final var allRestaurants = List.of(restaurant1, rest2, rest6, rest7);
    allRestaurants.forEach(restaurantEntity -> {
      final var table1 = new TableEntity(null, TableType.TWO_PERSON, 4, restaurantEntity, null);
      final var book1 = new BookEventEntity(null, Instant.parse("2024-12-06T18:00:00Z"),
        restaurantEntity.getUser(), table1);
      final var book2 = new BookEventEntity(null, Instant.parse("2024-12-07T13:00:00Z"),
        restaurantEntity.getUser(), table1);
      final var book3 = new BookEventEntity(null, Instant.parse("2024-12-07T16:00:00Z"),
        restaurantEntity.getUser(), table1);

      final var table2 = new TableEntity(null, TableType.FOUR_PERSON, 2, restaurantEntity, null);
      final var book4 = new BookEventEntity(null, Instant.parse("2024-12-08T12:00:00Z"),
        restaurantEntity.getUser(), table2);
      final var book5 = new BookEventEntity(null, Instant.parse("2024-12-08T16:00:00Z"),
        restaurantEntity.getUser(), table2);

      final var table3 = new TableEntity(null, TableType.EIGHT_PERSON, 1, restaurantEntity, null);
      final var book6 = new BookEventEntity(null, Instant.parse("2024-12-07T15:00:00Z"),
        restaurantEntity.getUser(), table3);
      tableRepository.saveAll(List.of(table1, table2, table3));
      tableRepository.flush();
      bookEventRepository.saveAll(List.of(book1, book2, book3, book4, book5, book6));
      bookEventRepository.flush();
    });
  }
}
