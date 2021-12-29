package com.app.apigeochat.controller.user;

import com.app.apigeochat.controller.map.MarkerController;
import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.dto.UserAuthDto;
import com.app.apigeochat.dto.UserProvidingDto;
import com.app.apigeochat.service.chat.EncryptionService;
import com.app.apigeochat.service.chat.FriendService;
import com.app.apigeochat.service.chat.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;
    private final EncryptionService encryptionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerController.class);
    private static final String CREATE_USER_LOG_MESSAGE = "New user was created: {}";
    private static final String REMOVE_USER_LOG_MESSAGE = "User was removed: {}";

    @Autowired
    public UserController(
            UserService userService,
            FriendService friendService,
            EncryptionService encryptionService
    ) {
        this.userService = userService;
        this.friendService = friendService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email
    ) {
        final var userId = userService.create(name, email, password);
        if (userId.isPresent()) {
            LOGGER.info(CREATE_USER_LOG_MESSAGE, userId.get());
            return ResponseEntity.ok(userId.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "User with these parameters are already created"
            );
        }
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
        if (user.isPresent()) {
            return ResponseEntity.ok(new UserProvidingDto(user.get()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<UserProvidingDto>> searchByName(@RequestParam("substring") String substring) {
        List<UserProvidingDto> foundUsers = Collections.emptyList();
        if (!substring.isEmpty()) {
            return ResponseEntity.ok(userService
                    .searchByName(substring)
                    .stream().map(UserProvidingDto::new)
                    .collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(foundUsers);
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("userId") String userId) {
        LOGGER.info(REMOVE_USER_LOG_MESSAGE, userId);
        userService.remove(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/inviteToFriends")
    public ResponseEntity<Void> inviteToFriends(
            @RequestParam("invitedUserId") String invitedUserId,
            @RequestParam("invitingUserId") String invitingUserId
    ) {
        if (friendService.isFriends(UUID.fromString(invitedUserId), UUID.fromString(invitingUserId))) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Users are already friends");
        }

        if (friendService.inviteToFriends(UUID.fromString(invitedUserId), UUID.fromString(invitingUserId))) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occures while inviting a friend");
        }
    }

    @PostMapping("/acceptInvite")
    public ResponseEntity<Void> acceptInvite(
            @RequestParam("invitedUserId") String invitedUserId,
            @RequestParam("invitingUserId") String invitingUserId
    ) {
        if (friendService.acceptInvite(UUID.fromString(invitedUserId), UUID.fromString(invitingUserId))) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while accepting a invite");
        }
    }

    @PostMapping("/rejectInvite")
    public ResponseEntity<Void> rejectInvite(
            @RequestParam("invitedUserId") String invitedUserId,
            @RequestParam("invitingUserId") String invitingUserId
    ) {
        if (friendService.rejectInvite(UUID.fromString(invitedUserId), UUID.fromString(invitingUserId))) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while rejecting a invite");
        }
    }

    @PostMapping("/removeFriend")
    public ResponseEntity<Void> removeFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId
    ) {
        if (friendService.removeFriend(UUID.fromString(userId), UUID.fromString(friendId))) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while deleting a user");
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserProvidingDto>> getAllFriends(@RequestParam("userId") String userId) {
        final var optionalUser = userService.getById(UUID.fromString(userId));

        if (optionalUser.isPresent()) {
            List<UserProvidingDto> friends = optionalUser.get()
                    .getFriends()
                    .stream().map(UserProvidingDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(friends);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/invites")
    public ResponseEntity<List<UserProvidingDto>> getAllInvites(@RequestParam("userId") String userId) {
        final var optionalUser = userService.getById(UUID.fromString(userId));

        if (optionalUser.isPresent()) {
            List<UserProvidingDto> invites = optionalUser.get()
                    .getInvites()
                    .stream().map(UserProvidingDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(invites);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private ResponseEntity<UserAuthDto> generateUserAuthResponse(Optional<User> user, String password) {
        if (user.isPresent() &&
                Objects.equals(
                        user.get().getPassword(),
                        encryptionService.encrypt(password)
                )
        ) {
            return ResponseEntity.ok(new UserAuthDto(user.get()));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication failed");
        }
    }
}
