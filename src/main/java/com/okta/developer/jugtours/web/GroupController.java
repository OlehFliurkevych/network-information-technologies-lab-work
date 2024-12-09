package com.okta.developer.jugtours.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
class GroupController {

//  private final Logger log = LoggerFactory.getLogger(GroupController.class);
//  private GroupRepository groupRepository;
//  private UserRepository userRepository;
//
//  public GroupController(GroupRepository groupRepository, UserRepository userRepository) {
//    this.groupRepository = groupRepository;
//    this.userRepository = userRepository;
//  }
//
//  @GetMapping
//  Collection<Group> groups(Principal principal) {
//    return groupRepository.findAllByUserId(principal.getName());
//  }
//
//  @GetMapping("/{id}")
//  ResponseEntity<?> getGroup(@PathVariable Long id) {
//    Optional<Group> group = groupRepository.findById(id);
//    return group.map(response -> ResponseEntity.ok().body(response))
//      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//  }
//
//  @PostMapping
//  ResponseEntity<Group> createGroup(@Valid @RequestBody Group group,
//    @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
//    log.info("Request to create group: {}", group);
//    Map<String, Object> details = principal.getAttributes();
//    String userId = details.get("sub").toString();
//
//    // check to see if user already exists
//    Optional<User> user = userRepository.findById(userId);
//    group.setUser(user.orElse(new User(userId,
//      details.get("name").toString(), details.get("email").toString(), UserType.ADMIN,
//      Collections.emptyList(), Collections.emptyList())));
//
//    Group result = groupRepository.save(group);
//    return ResponseEntity.created(new URI("/api/group/" + result.getId()))
//      .body(result);
//  }
//
//  @PutMapping
//  ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) {
//    log.info("Request to update group: {}", group);
//    Group result = groupRepository.save(group);
//    return ResponseEntity.ok().body(result);
//  }
//
//  @DeleteMapping("/{id}")
//  public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
//    log.info("Request to delete group: {}", id);
//    groupRepository.deleteById(id);
//    return ResponseEntity.ok().build();
//  }

}
