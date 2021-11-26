package com.app.apigeochat.controller.user;

import com.app.apigeochat.domain.User;
import com.app.apigeochat.dto.UserProvidingDto;
import com.app.apigeochat.service.chat.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UUID create(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email
    ) {
        return this.userService.create(name, email, password);
    }

    @PostMapping("/auth")
    public User authByEmail(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        User user = userService.getByEmail(email);
        if (Objects.equals(user.getPassword(), password)) {
            return user;
        } else {
            throw new ResourceAccessException("Incorrect password");
        }
    }

    @PostMapping("/auth")
    public User authByName(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {
        User user = userService.getByName(name);
        if (Objects.equals(user.getPassword(), password)) {
            return user;
        } else {
            throw new ResourceAccessException("Incorrect password");
        }
    }

    @GetMapping("/getById")
    public UserProvidingDto getById(@RequestParam("userId") String userId) {
        return new UserProvidingDto(userService.getById(UUID.fromString(userId)));
    }

    @PostMapping("/remove")
    public void remove(@RequestParam("userId") String userId) {
        userService.remove(UUID.fromString(userId));
    }
}
