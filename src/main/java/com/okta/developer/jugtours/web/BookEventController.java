package com.okta.developer.jugtours.web;

import com.okta.developer.jugtours.exception.ValidationCustomException;
import com.okta.developer.jugtours.model.BookEventDto;
import com.okta.developer.jugtours.model.BookEventEntity;
import com.okta.developer.jugtours.model.BookEventModel;
import com.okta.developer.jugtours.model.TableEntity;
import com.okta.developer.jugtours.repository.BookEventRepository;
import com.okta.developer.jugtours.repository.TableRepository;
import com.okta.developer.jugtours.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/events")
public class BookEventController {

  private final Logger log = LoggerFactory.getLogger(TableController.class);
  private final BookEventRepository bookEventRepository;
  private final TableRepository tableRepository;
  private final UserRepository userRepository;

  public BookEventController(BookEventRepository bookEventRepository,
    TableRepository tableRepository,
    UserRepository userRepository) {
    this.bookEventRepository = bookEventRepository;
    this.tableRepository = tableRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<BookEventModel> events(Principal principal) {
    final var user = userRepository.findById(principal.getName()).get();
    return bookEventRepository.findAllByUserId(principal.getName()).stream()
      .map(entity -> {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final var dateFormated = format.format(entity.getDate());
        final var restaurant = entity.getTable().getRestaurant();
        return new BookEventModel(entity.getId(), user.getName(), dateFormated, restaurant,
          entity.getTable());
      }).toList();
  }

  @GetMapping("/{id}")
  ResponseEntity<?> getEvent(@PathVariable Long id) {
    Optional<TableEntity> group = tableRepository.findById(id);
    return group.map(response -> ResponseEntity.ok().body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  ResponseEntity<BookEventModel> createEvent(@Valid @RequestBody BookEventDto event,
    @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException, ParseException {
    log.info("Request to create event: {}", event);

    Map<String, Object> details = principal.getAttributes();
    String userId = details.get("sub").toString();

    var user = userRepository.findById(userId).get();
    var table = tableRepository.findById(event.getTable()).get();

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String dateString = event.getDate().toString() + " " + event.getTime();
    Date date = format.parse(dateString);

    if (bookEventRepository.findAllByTableIdAndDate(event.getTable(), date).size()
        == table.getCount()) {
      throw new ValidationCustomException(
        String.format(
          "There are no available tables with type '%s' in restaurant '%s' at that time",
          table.getTableType().name(), table.getRestaurant().getName()));
    }

    var bookEvent = new BookEventEntity();
    bookEvent.setDate(date);
    bookEvent.setUser(user);
    bookEvent.setTable(table);
    BookEventEntity result = bookEventRepository.save(bookEvent);
    final var model = new BookEventModel(result.getId(), user.getName(),
      format.format(result.getDate()), result.getTable().getRestaurant(), result.getTable());
    return ResponseEntity.created(new URI("/api/events/" + model.getId())).body(model);
  }

  @PutMapping
  ResponseEntity<BookEventModel> updateEvent(@Valid @RequestBody BookEventEntity event) {
    log.info("Request to update event: {}", event);
    BookEventEntity result = bookEventRepository.save(event);
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    final var model = new BookEventModel(result.getId(), result.getUser().getName(),
      format.format(result.getDate()), result.getTable().getRestaurant(), result.getTable());
    return ResponseEntity.ok().body(model);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
    log.info("Request to delete event: {}", id);
    bookEventRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
