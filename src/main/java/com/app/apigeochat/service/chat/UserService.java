package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.Role;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    public final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public Optional<User> getById(UUID id) {
        return userRepo.findById(id);
    }

    public Optional<User> getByName(String name) {
        return userRepo.findByName(name);
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<UUID> create(String username, String email, String password) {
        if (userRepo.existsByName(username) || userRepo.existsByEmail(email)) {
            return Optional.empty();
        } else {
            User user = new User();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.User);
            return Optional.of(userRepo.save(user).getUserId());
        }
    }

    public boolean remove(UUID id) {
        return userRepo.findAll().removeIf(user -> user.getUserId() == id);
    }
}
