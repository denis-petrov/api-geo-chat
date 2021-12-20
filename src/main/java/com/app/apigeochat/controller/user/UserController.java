package com.app.apigeochat.controller.user;

import com.app.apigeochat.controller.map.MarkerController;
import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.dto.UserAuthDto;
import com.app.apigeochat.dto.UserProvidingDto;
import com.app.apigeochat.service.chat.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerController.class);
    private static final String CREATE_USER_LOG_MESSAGE = "New user was created: {}";
    private static final String REMOVE_USER_LOG_MESSAGE = "User was removed: {}";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email
    ) {
        return userService.create(name, email, password)
                .map(uuid -> {
                    LOGGER.info(CREATE_USER_LOG_MESSAGE, uuid);
                    return ResponseEntity.ok(uuid);
                })
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/authByEmail")
    public ResponseEntity<UserAuthDto> authByEmail(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        final var user = userService.getByEmail(email);
        return generateUserAuthResponse(user, password);
    }

    @PostMapping("/authByName")
    public ResponseEntity<UserAuthDto> authByName(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {
        final var user = userService.getByName(name);
        return generateUserAuthResponse(user, password);
    }

    @GetMapping("/getById")
    public ResponseEntity<UserProvidingDto> getById(@RequestParam("userId") String userId) {
        final var user = userService.getById(UUID.fromString(userId));
        return user.map(value -> ResponseEntity.ok(new UserProvidingDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<UserProvidingDto>> searchByName(@RequestParam("substring") String substring) {
        List<UserProvidingDto> foundUsers = Collections.emptyList();
        if (!substring.isEmpty()) {
            foundUsers = userService.searchByName(substring)
                    .stream().map(UserProvidingDto::new)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(foundUsers);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("userId") String userId) {
        LOGGER.info(REMOVE_USER_LOG_MESSAGE, userId);
        userService.remove(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addFriend")
    public ResponseEntity<Void> addFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId
    ) {
        return userService.addFriend(UUID.fromString(userId), UUID.fromString(friendId))
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/removeFriend")
    public ResponseEntity<Void> removeFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId
    ) {
        return userService.removeFriend(UUID.fromString(userId), UUID.fromString(friendId))
                ? ResponseEntity.ok().build()
                : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserProvidingDto>> getAllFriends(@RequestParam("userId") String userId) {
        final var optionalUser = userService.getById(UUID.fromString(userId));

        return optionalUser
                .map(user -> {
                    List<UserProvidingDto> friends = user.getFriends()
                            .stream().map(UserProvidingDto::new)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(friends);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<UserAuthDto> generateUserAuthResponse(Optional<User> user, String password) {
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return Objects.equals(user.get().getPassword(), password)
                ? ResponseEntity.ok(new UserAuthDto(user.get()))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
