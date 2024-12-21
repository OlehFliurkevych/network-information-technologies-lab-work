package com.okta.developer.jugtours.web;

import com.okta.developer.jugtours.enums.UserType;
import com.okta.developer.jugtours.exception.ValidationCustomException;
import com.okta.developer.jugtours.model.RestaurantEntity;
import com.okta.developer.jugtours.model.TableEntity;
import com.okta.developer.jugtours.model.User;
import com.okta.developer.jugtours.repository.RestaurantRepository;
import com.okta.developer.jugtours.repository.TableRepository;
import com.okta.developer.jugtours.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {

  private final Logger log = LoggerFactory.getLogger(RestaurantController.class);
  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;
  private final TableRepository tableRepository;

  public RestaurantController(RestaurantRepository restaurantRepository,
    UserRepository userRepository, TableRepository tableRepository) {
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
    this.tableRepository = tableRepository;
  }

  @GetMapping
  public List<RestaurantEntity> restaurants(Principal principal) {
    return restaurantRepository.findAllByUserId(principal.getName());
  }

  @GetMapping("/{id}")
  ResponseEntity<?> getRestaurant(@PathVariable Long id) {
    Optional<RestaurantEntity> group = restaurantRepository.findById(id);
    return group.map(response -> ResponseEntity.ok().body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  ResponseEntity<RestaurantEntity> createRestaurant(@Valid @RequestBody RestaurantEntity restaurant,
    @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
    log.info("Request to create restaurant: {}", restaurant);
    Map<String, Object> details = principal.getAttributes();
    String userId = details.get("sub").toString();

    // check to see if user already exists
    Optional<User> user = userRepository.findById(userId);
    restaurant.setUser(user.orElse(new User(userId,
      details.get("name").toString(), details.get("email").toString(), UserType.ADMIN,
      Collections.emptyList(), Collections.emptyList())));

    if (!restaurantRepository.findAllByName(restaurant.getName()).isEmpty()) {
      throw new ValidationCustomException(String.format(
        "Restaurant with the same name '%s' have already existed. "
        + "Please choose another name or use existing restaurant.", restaurant.getName()));
    }

    RestaurantEntity result = restaurantRepository.save(restaurant);
    return ResponseEntity.created(new URI("/api/restaurant/" + result.getId())).body(result);
  }

  @PutMapping("/{id}")
  ResponseEntity<RestaurantEntity> updateRestaurant(@PathVariable("id") Long restaurantId,
    @Valid @RequestBody RestaurantEntity restaurant,
    @AuthenticationPrincipal OAuth2User principal) {
    log.info("Request to update restaurant: {}", restaurant);

    final var optional = restaurantRepository.findById(restaurantId);
    if (optional.isPresent()) {
      Map<String, Object> details = principal.getAttributes();
      String userId = details.get("sub").toString();
      final var user = userRepository.findById(userId).get();
      restaurant.setUser(user);
      RestaurantEntity result = restaurantRepository.save(restaurant);
      return ResponseEntity.ok().body(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
    log.info("Request to delete restaurant: {}", id);

    final var tables = tableRepository.findAllByRestaurantId(id);
    final var rest = restaurantRepository.findById(id).get();
    if (!tables.isEmpty()) {
      throw new ValidationCustomException(String.format(
        "Restaurant '%s' can't be deleted. This restaurant contains not deleted tables.",
        rest.getName()));
    }

    restaurantRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{restaurantId}/tables")
  public List<TableEntity> tables(@PathVariable("restaurantId") Long id) {
    return tableRepository.findAllByRestaurantId(id);
  }

}
