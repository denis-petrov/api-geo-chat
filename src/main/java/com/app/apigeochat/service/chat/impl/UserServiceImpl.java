package com.app.apigeochat.service.chat.impl;

import com.app.apigeochat.domain.Role;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.UserRepository;
import com.app.apigeochat.service.chat.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public User get(UUID id) {
        return userRepo.getById(id);
    }

    @Override
    public User get(String name) {
        return userRepo.getByName(name);
    }

    @Override
    public UUID create(String username, String email, String password) {
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.User);
        return userRepo.save(user).getUserId();
    }

    @Override
    public void remove(UUID id) {
        userRepo.deleteById(id);
    }
}
