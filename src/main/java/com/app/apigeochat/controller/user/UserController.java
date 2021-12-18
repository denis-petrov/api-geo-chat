package com.app.apigeochat.controller.user;

import com.app.apigeochat.domain.User;
import com.app.apigeochat.dto.UserProvidingDto;
import com.app.apigeochat.service.chat.UserService;
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
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/authByEmail")
    public ResponseEntity<User> authByEmail(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        Optional<User> user = userService.getByEmail(email);

        if (user.isPresent()) {
            if (Objects.equals(user.get().getPassword(), password)) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/authByName")
    public ResponseEntity<User> authByName(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {
        Optional<User> user = userService.getByName(name);

        if (user.isPresent()) {
            if (Objects.equals(user.get().getPassword(), password)) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<UserProvidingDto> getById(@RequestParam("userId") String userId) {
        Optional<User> user = userService.getById(UUID.fromString(userId));
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
        userService.remove(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addFriend")
    public ResponseEntity<Void> addFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId
    ) {
        if (userService.addFriend(
                UUID.fromString(userId),
                UUID.fromString(friendId))
        ) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/removeFriend")
    public ResponseEntity<Void> removeFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId
    ) {
        if (userService.removeFriend(
                UUID.fromString(userId),
                UUID.fromString(friendId))
        ) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<Set<User>> getAllFriends(@RequestParam("userId") String userId) {
        Optional<User> optionalUser = userService.getById(UUID.fromString(userId));

        return optionalUser
                .map(user -> ResponseEntity.ok(user.getFriends()))
                .orElse(ResponseEntity.notFound().build());
    }
}
