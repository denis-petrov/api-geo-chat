package com.app.apigeochat.controller.user;

import com.app.apigeochat.domain.User;
import com.app.apigeochat.dto.UserCreationDto;
import com.app.apigeochat.service.chat.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UUID create(@RequestBody UserCreationDto userDto) {
        return this.userService.create(userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }

    @GetMapping("/getByName")
    public User getByName(@RequestParam("name") String name) {
        return userService.get(name);
    }

    @GetMapping("/getById")
    public User getById(@RequestParam("userId") UUID userId) {
        return userService.get(userId);
    }

    @PostMapping("/remove")
    public void remove(@RequestParam("userId") UUID userId) {
        userService.remove(userId);
    }
}
