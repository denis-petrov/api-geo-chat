package com.app.apigeochat.service.chat.impl;

import com.app.apigeochat.domain.Role;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.UserRepository;
import com.app.apigeochat.service.chat.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public User getById(UUID id) {
        return userRepo.getById(id);
    }

    @Override
    public User getByName(String name) {
        return userRepo.getByName(name);
    }

    @Override
    public User getByEmail(String email) {
        return userRepo.getByEmail(email);
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
