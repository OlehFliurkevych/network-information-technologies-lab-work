package com.okta.developer.jugtours.web;

import com.okta.developer.jugtours.model.BookEventEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
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
  public List<BookEventEntity> events(Principal principal) {
    return bookEventRepository.findAllByUserId(principal.getName());
  }

  @GetMapping("/{id}")
  ResponseEntity<?> getEvent(@PathVariable Long id) {
    Optional<TableEntity> group = tableRepository.findById(id);
    return group.map(response -> ResponseEntity.ok().body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  ResponseEntity<BookEventEntity> createEvent(@Valid @RequestBody BookEventEntity event,
    @AuthenticationPrincipal OAuth2User principal,
    @RequestParam(name = "table_id") Long tableId) throws URISyntaxException {
    log.info("Request to create event: {}", event);

    Map<String, Object> details = principal.getAttributes();
    String userId = details.get("sub").toString();

    var user = userRepository.findById(userId).get();
    var table = tableRepository.findById(tableId).get();
    event.setTable(table);
    event.setUser(user);

    BookEventEntity result = bookEventRepository.save(event);
    return ResponseEntity.created(new URI("/api/events/" + result.getId())).body(result);
  }

  @PutMapping
  ResponseEntity<BookEventEntity> updateEvent(@Valid @RequestBody BookEventEntity event) {
    log.info("Request to update event: {}", event);
    BookEventEntity result = bookEventRepository.save(event);
    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
    log.info("Request to delete event: {}", id);
    bookEventRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
